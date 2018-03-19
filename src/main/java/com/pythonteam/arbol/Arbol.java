package com.pythonteam.arbol;

import com.pythonteam.archivos.ArchivoIndice;
import com.pythonteam.common.Constantes;

import java.util.List;
import java.util.Objects;

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
            List<Indice> dirReglas = index.getDirRegistros();
            dirReglas.stream().filter(Objects::nonNull).map(direccion -> new Nodo((byte) direccion.getLlave(), direccion.getDireccion())).forEachOrdered(this::insertarNodo);
        } catch (Exception ex) {
            System.out.println("Fallo al crear el arbolito\n"+ex.getMessage());
        }
    }
}
