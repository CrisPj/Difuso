package com.pythonteam.archivos;

import com.pythonteam.arbol.Arbol;
import com.pythonteam.arbol.Indice;
import com.pythonteam.arbol.Variable;
import com.pythonteam.common.Constantes;

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
                    String[] registros = new String[5];
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
                    variables.add(variable);
                } while (true);
        } catch (Exception ex) {
            System.out.println("Se han cargado las variables: " + ex.getMessage());
        }
    }

    public void nuevoRegistro(Variable var) {
        variables.add(var);
        StringBuffer buffer;
        try {
            archivo.seek(archivo.length());
            index.nuevo(var.getId(), archivo.getFilePointer());
            archivo.writeByte(var.getId());
            buffer = new StringBuffer(var.getNombre());
            buffer.setLength(Constantes.TAM_REGISTRO);
            archivo.writeChars(buffer.toString());
            buffer = new StringBuffer(var.getAlias());
            buffer.setLength(Constantes.TAM_REGISTRO);
            archivo.writeChars(buffer.toString());
        } catch (Exception ex) {
            System.out.println("Fallo al escribir en archivo maestro");
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
                index.nuevo(v.getId(), archivo.getFilePointer());
                archivo.writeByte(v.getId());
                buffer = new StringBuffer(v.getNombre());
                buffer.setLength(Constantes.TAM_REGISTRO);
                archivo.writeChars(buffer.toString());

                buffer = new StringBuffer(v.getAlias());
                buffer.setLength(Constantes.TAM_REGISTRO);
                archivo.writeChars(buffer.toString());
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
