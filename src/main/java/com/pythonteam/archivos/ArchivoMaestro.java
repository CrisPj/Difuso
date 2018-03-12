package com.pythonteam.archivos;

import com.pythonteam.arbol.Arbol;
import com.pythonteam.arbol.Indice;
import com.pythonteam.arbol.Regla;
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
    private ArrayList<Regla> reglas;


    public ArchivoMaestro(String nombre, String permisos) {
        reglas = new ArrayList<>();
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
                    Regla regla = new Regla();
                    String[] registros = new String[Regla.CANTIDAD_REGISTROS];
                    char[] registroActual = new char[Regla.TAM_REGISTRO];
                    regla.setLlave(archivo.readByte());
                    for (int recordNumber = 0; recordNumber < Regla.CANTIDAD_REGISTROS; recordNumber++) {
                        for (int i = 0; i < Regla.TAM_REGISTRO; i++) {
                            registroActual[i] = archivo.readChar();
                        }
                        registros[recordNumber] = new String(registroActual).trim();
                    }
                    regla.setReglas(registros);

                    for (int i = 0; i < Regla.TAM_REGISTRO; i++) {
                        registroActual[i] = archivo.readChar();
                    }
                    regla.setConsecuente(new String(registroActual).trim());
                    reglas.add(regla);
                } while (true);
        } catch (Exception ex) {
            System.out.println("Se han cargado las reglas: " + ex.getMessage());
        }
    }

    public void nuevoRegistro(Regla regla) {
        reglas.add(regla);
        StringBuffer buffer;
        try {
            archivo.seek(archivo.length());
            index.nuevo(regla.getLlave(), archivo.getFilePointer());
            archivo.writeByte(regla.getLlave());
            writeRule(regla);
            buffer = new StringBuffer(regla.getConsecuente());
            buffer.setLength(Regla.TAM_REGISTRO);
            archivo.writeChars(buffer.toString());
        } catch (Exception ex) {
            System.out.println("Fallo al escribir en archivo maestro");
        }
    }

    private void writeRule(Regla regla) throws IOException {
        StringBuffer buffer;
        for (int i = 0; i < Regla.CANTIDAD_REGISTROS; i++) {
            try {
                buffer = new StringBuffer(regla.getReglas()[i]);
            } catch (Exception ex) {
                buffer = new StringBuffer();
            }
            buffer.setLength(Regla.TAM_REGISTRO);
            archivo.writeChars(buffer.toString());
        }
    }

    public Regla obtenerRegla(Integer numeroRegla) {
        if (numeroRegla > 0)
            for (Regla r : reglas) {
                if (r.getLlave() == numeroRegla)
                    return r;
            }
        return null;
    }

    public ArrayList<Regla> imprimirReglas() {
        return reglas;
    }

    public List<Indice> mostrarIndex() {
        return index.mostrarIndice();
    }

    public void generarArbol() {
        Arbol = new Arbol();
        Arbol.generarArbol();
    }

    public boolean eliminarRegla(int llave) {
        reglas.removeIf(r-> r.getLlave() == llave);
        writeFile();
        return false;
    }

    private void writeFile() {
        eliminarReglas();
        StringBuffer buffer;
        try {
            archivo.seek(0);
            for (Regla regla : reglas) {
                index.nuevo(regla.getLlave(), archivo.getFilePointer());
                archivo.writeByte(regla.getLlave());
                writeRule(regla);
                buffer = new StringBuffer(regla.getConsecuente());
                buffer.setLength(Regla.TAM_REGISTRO);
                archivo.writeChars(buffer.toString());
            }
        } catch (Exception ex) {
            System.out.println("Fallo al escribir en archivo maestro");
        }
    }


    public boolean eliminarTodo()
    {
        eliminarReglas();
        reglas.clear();
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
