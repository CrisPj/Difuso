
package com.pythonteam.archivos;

import com.pythonteam.arbol.Indice;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PythonTeam :v
 */
public class ArchivoIndice
{

    private RandomAccessFile archivo;
    private List<Indice> indices;

    /**
     * @param nombre Nombre del archivo
     * @param permisos Permisos que tendra el archivo
     */
    public ArchivoIndice(String nombre, String permisos)
    {
        indices = new ArrayList<>();
        try {
            archivo = new RandomAccessFile(nombre, permisos);
            if (archivo.length() > 0)
                readFile();
        } catch (Exception ex) {
            System.out.println("Fallo al crear el archivo indice\n"+ ex.getMessage());
        }
    }

    /**
     *
     * Escribe un nuevo registro, especificando
     *
     * @param llave del registro
     * @param dirLogica del registro
     */
    public void nuevo(int llave, long dirLogica) {
        indices.add(new Indice(llave,dirLogica));
        try {
            archivo.seek(archivo.length());
            archivo.writeByte(llave);
            archivo.writeLong(dirLogica);
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
    }

    /**
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getDirRegistros() {
        ArrayList<String> reglas = new ArrayList<>();
        for (Indice indice : indices)
        {
            reglas.add(indice.getLlave() + "-" + indice.getDireccion());
        }
        return reglas;
    }

    public List<Indice> mostrarIndice() {
            return indices;
    }

    public long buscar(int llave)
    {
        for (Indice indice : indices)
            if (indice.getLlave() == llave)
                return indice.getDireccion();
        return -1;
    }

    private void readFile()
    {
        try {
            archivo.seek(0);
            do {

                indices.add(new Indice(archivo.readByte(), archivo.readLong()));
            }while (true);
        } catch (IOException e) {
            System.out.println("Se ha leido el archivo de indices por completo");
        }
    }

    public void writeFile()
    {
        limpiarArchivo();
        try {
            archivo.seek(0);
            for (Indice indice : indices) {
                archivo.writeByte(indice.getLlave());
                archivo.writeLong(indice.getDireccion());
            }
        } catch (IOException e) {
            System.out.println("Ha habido un error al procesar el archivo de indcies");
            System.exit(0);
        }
    }

    public void eliminar(int llave)
    {
        indices.removeIf(e-> e.getLlave() == llave);
    }

    public void limpiarLista()
    {
        indices.clear();
    }

    public void limpiarTodo()
    {
        limpiarArchivo();
        limpiarLista();
    }


    public void limpiarArchivo() {
        try {
            archivo.setLength(0);
        } catch (Exception ex) {
            System.out.println("Archivo no puede ser limpiado");
        }
    }
}
