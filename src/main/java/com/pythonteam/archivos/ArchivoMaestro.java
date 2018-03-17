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
    private String ruta;
    private ArrayList<Variable> variables;


    public ArchivoMaestro(String nombre, String permisos) {
        variables = new ArrayList<>();
        crearArchivo(nombre, permisos);
    }


    public void crearArchivo(String nombre, String permisos) {
        try {
            ruta = nombre;
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
                do {
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

                    int tam = archivo.readInt();
                    ArrayList<Funcion> func = new ArrayList<>();
                    for (int i = 0; i < tam; i++) {
                        Funcion f = new Funcion();
                        char[] nombre = new char[Constantes.TAM_REGISTRO];
                        for (int j = 0; j < Constantes.TAM_REGISTRO; j++) {
                            nombre[i] = archivo.readChar();
                        }
                        f.setNombre(new String(nombre).trim());
                        f.setTranslape(archivo.readInt());
                        int size = archivo.readInt();

                        double puntos[] = new double[size];
                        for (int p = 0; p < size; p++) {
                            puntos[p] = archivo.readDouble();
                        }
                        f.setPuntoCritico(puntos);

                    }

                    archivo.readChar();
                    variable.setFunciones(func);

                    variables.add(variable);
                } while (true);
        } catch (Exception ex) {
            System.out.println("Se han cargado las variables: " + ex.getMessage());
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
        int tam = var.getFunciones().size();
        archivo.writeInt(tam);
        if (tam > 0)
        {
            for (Funcion f:var.getFunciones())
            {
                buffer = new StringBuffer(f.getNombre());
                buffer.setLength(Constantes.TAM_REGISTRO);
                archivo.writeChars(buffer.toString());

                archivo.writeInt(f.getTranslape());
                archivo.writeInt(f.getPuntoCritico().length);
                for (int i = 0; i < f.getPuntoCritico().length; i++) {
                    archivo.writeDouble(f.getPuntoCritico()[i]);
                }
            }
            archivo.writeChar('!');
        }
    }

    public Variable obtenerRegla(Integer numeroRegla) {
        if (numeroRegla > 0)
            for (Variable v : variables) {
                if (v.getId() == numeroRegla)
                    return v;
            }
        return null;
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

    private void writeFile() {
        eliminarReglas();
        StringBuffer buffer;
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
