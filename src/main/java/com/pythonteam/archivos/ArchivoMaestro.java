package com.pythonteam.archivos;

import com.pythonteam.arbol.Arbol;
import com.pythonteam.arbol.Funcion;
import com.pythonteam.arbol.Indice;
import com.pythonteam.arbol.Variable;
import com.pythonteam.common.Constantes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ArchivoMaestro {

    private RandomAccessFile archivo;
    private ArchivoIndice index;
    private Arbol Arbol;
    private ArrayList<Variable> variables;


    public ArchivoMaestro(String nombre, String permisos) {
        variables = new ArrayList<>();
        crearArchivo(nombre, permisos);
    }


    public void crearArchivo(String nombre, String permisos) {
        try {
            archivo = new RandomAccessFile(nombre + Constantes.EXTENCION_CONOCIMIENTO, permisos);
            index = new ArchivoIndice(nombre + Constantes.EXTENCION_INDICE, permisos);
            if (archivo.length() > 0) {
                readFile();
            }
        } catch (Exception ex) {
            System.out.println("Fallo al crear archivo maestro");
        }
    }

    public void readFile() {
        try {
                archivo.seek(0);
            for (int x = 0; x < archivo.length(); x++) {

                    Variable variable = new Variable();
                    char[] registroActual = new char[Constantes.TAM_REGISTRO];
                    variable.setId(archivo.readByte());
                    for (int i = 0; i < Constantes.TAM_REGISTRO; i++) {
                        registroActual[i] = archivo.readChar();
                    }
                    variable.setNombre(new String(registroActual).trim());
                    for (int i = 0; i < Constantes.TAM_REGISTRO; i++) {
                        registroActual[i] = archivo.readChar();
                    }
                    variable.setAlias(new String(registroActual).trim());
                    variable.setSalida(archivo.readBoolean());

                    int tam = archivo.readInt();
                    ArrayList<Funcion> func = new ArrayList<>();
                    for (int i = 0; i < tam; i++) {
                        Funcion f = new Funcion();
                        char[] nombre = new char[Constantes.TAM_REGISTRO];
                        for (int j = 0; j < Constantes.TAM_REGISTRO; j++) {
                            nombre[j] = archivo.readChar();
                        }
                        f.setNombre(new String(nombre).trim());
                        f.setTraslape(archivo.readInt());
                        int size = archivo.readInt();

                        double puntos[] = new double[size];
                        for (int p = 0; p < size; p++) {
                            puntos[p] = archivo.readDouble();
                        }
                        f.setPuntoCritico(puntos);
                        func.add(f);

                    }
                    variable.setFunciones(func);

                    variables.add(variable);
                }
        } catch (Exception ex) {
            System.out.println("No se pudo leer algo: " + ex.getMessage());
        }
    }

    public void nuevoRegistro(Variable var) {
        variables.add(var);

        try {
            escribir(var);
        } catch (Exception ex) {
            System.out.println("Fallo al escribir en archivo maestro");
        }
    }

    private void escribir(Variable var) throws IOException {
        StringBuffer buffer;
        archivo.seek(archivo.length());
        index.nuevo(var.getId(), archivo.getFilePointer());
        archivo.writeByte(var.getId());
        buffer = new StringBuffer(var.getNombre());
        buffer.setLength(Constantes.TAM_REGISTRO);
        archivo.writeChars(buffer.toString());
        buffer = new StringBuffer(var.getAlias());
        buffer.setLength(Constantes.TAM_REGISTRO);
        archivo.writeChars(buffer.toString());
        archivo.writeBoolean(var.isSalida());
        int tam = var.getFunciones().size();
        archivo.writeInt(tam);
        if (tam > 0)
        {
            for (Funcion f:var.getFunciones())
            {
                buffer = new StringBuffer(f.getNombre());
                buffer.setLength(Constantes.TAM_REGISTRO);
                archivo.writeChars(buffer.toString());

                archivo.writeInt(f.getTraslape());
                archivo.writeInt(f.getPuntoCritico().length);
                for (int i = 0; i < f.getPuntoCritico().length; i++) {
                    archivo.writeDouble(f.getPuntoCritico()[i]);
                }
            }
        }
    }

    public Variable obtenerRegla(Integer numeroRegla) {
        return variables.stream().filter(v -> v.getId() == numeroRegla).findFirst().orElse(null);
    }

    public ArrayList<Variable> imprimirReglas() {
        return variables;
    }

    public List<Indice> mostrarIndex() {
        return index.mostrarIndice();
    }

    public void generarArbol() {
        Arbol = new Arbol();
        Arbol.generarArbol();
    }

    public boolean eliminarRegla(int id) {
        variables.removeIf(r-> r.getId() == id);
        writeFile();
        return false;
    }

    public boolean editarRegla(Variable var)
    {
        Variable vr = variables.get(var.getId());
        vr.setId(var.getId());
        vr.setNombre(var.getNombre());
        vr.setSalida(var.isSalida());
        vr.setAlias(var.getAlias());
        vr.setFunciones(var.getFunciones());
        writeFile();
        return true;
    }

    private void writeFile() {
        eliminarReglas();
        try {
            archivo.seek(0);
            for (Variable v : variables) {
               escribir(v);
            }
        } catch (Exception ex) {
            System.out.println("Fallo al escribir en archivo maestro");
        }
    }


    public boolean eliminarTodo()
    {
        eliminarReglas();
        variables.clear();
        return true;
    }

    public void eliminarReglas() {
        try {
            archivo.setLength(0);
            index.limpiarArchivo();
        } catch (Exception ex) {
            System.out.println("Archivo no pudo ser eliminado");
        }
    }
}
