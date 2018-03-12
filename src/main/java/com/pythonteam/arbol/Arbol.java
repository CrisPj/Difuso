package com.pythonteam.arbol;

import com.pythonteam.archivos.ArchivoIndice;
import com.pythonteam.common.Constantes;

import java.util.ArrayList;

public class Arbol
{
    private Nodo arbol;

    private void insertarNodo(Nodo nodo) {
        if (arbol != null)
            arbol.insertarNodo(nodo);
        else
            arbol = nodo;
    }

    public long getReglaDirLogica(Integer llave) {
        return buscarReglaDirLogica(llave, arbol);
    }

    private long buscarReglaDirLogica(long llave, Nodo raiz) {
        if (raiz.getLlave() == llave)
            return raiz.getDirLogica();
        else if (raiz.getLlave() > llave)
            return raiz.izquierdo != null ? buscarReglaDirLogica(llave, raiz.izquierdo) : -1;
        else if (raiz.derecho != null)
            return buscarReglaDirLogica(llave, raiz.derecho);
        else
            return -1;

    }

    public void generarArbol() {
        try {
            ArchivoIndice index = new ArchivoIndice(Constantes.NOMBRE_ARCHIVOS + Constantes.EXTENCION_INDICE, Constantes.LECTURA_ESCRITURA);
            ArrayList<String> dirReglas = index.getDirRegistros();
            byte contador = 0;
            do {
                String direccion = dirReglas.get(contador);
                if (direccion != null) {
                    Nodo nodo = new Nodo(Byte.parseByte(direccion.split("-")[0]), Long.parseLong(direccion.split("-")[1]));
                    insertarNodo(nodo);
                }
                contador++;
            } while (contador < dirReglas.size());
        } catch (Exception ex) {
            System.out.println("Fallo al crear el arbolito\n"+ex.getMessage());
        }
    }
}
