package com.pythonteam.archivos;

import com.pythonteam.common.Constantes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ArchivoHechos
{

    private RandomAccessFile file;
    private ArrayList<String> hechos;

    public ArchivoHechos(String nombre, String permisos) {
        hechos = new ArrayList<>();
        try {
            file = new RandomAccessFile(nombre + Constantes.EXTENCION_HECHOS, permisos);
            if (file.length() > 0)
                readFile();
        } catch (Exception ex) {
            System.out.println("Archivo no pudo ser creado");
        }
    }

    private void readFile() {
        try {
            file.seek(0);
            do {

                char[] hecho = new char[Constantes.TAM_REGISTRO];
                for (int i = 0; i < Constantes.TAM_REGISTRO; i++)
                    hecho[i] = file.readChar();
                hechos.add(new String(hecho).trim());
            }while (true);
        } catch (IOException e) {
            System.out.println("Se ha leido el archivo de indices por completo");
        }
    }

    public void insertarHecho(String hecho) {
        hechos.add(hecho);
        try {
            StringBuilder buffer = new StringBuilder(hecho);
            buffer.setLength(Constantes.TAM_REGISTRO);
            file.writeChars(buffer.toString());
        } catch (Exception ex) {
            System.out.println("No se pudo insertar hecho");
        }
    }

    public ArrayList<String> obtenerHechos() {
        return hechos;
    }

    public String imprimirHechos() {
        StringBuilder retorno = new StringBuilder();
        for (String hecho : obtenerHechos())
            retorno.append(hecho).append("\n");
        return retorno.toString();
    }


    public void writeFile()
    {
        borrarHechos();
        for (String hecho : hechos) {
            StringBuilder buffer = new StringBuilder(hecho);
            buffer.setLength(Constantes.TAM_REGISTRO);
            try {
                file.writeChars(buffer.toString());
            } catch (IOException e) {
                System.exit(0);
            }
        }
    }

    public void borrarHecho(String borrar)
    {
        hechos.removeIf(e -> e.equals(borrar));
    }

    public void borrarHechos() {
        hechos.clear();
        try {
            file.setLength(0);
        } catch (Exception ex) {
            System.out.println("Hechos no pudieron ser eliminados");
        }
    }

}
