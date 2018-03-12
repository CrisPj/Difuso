package com.pythonteam.arbol;

public class Nodo
{

    protected Nodo izquierdo;
    protected Nodo derecho;
    private byte llave;
    private long dirLogica;

    public Nodo(byte llave, long dirLogica) {
        this.llave = llave;
        this.dirLogica = dirLogica;
    }

    public void insertarNodo(Nodo nodo) {
        if (llave > nodo.getLlave()) if (izquierdo == null) izquierdo = nodo;
        else izquierdo.insertarNodo(nodo);
        else if (derecho == null) derecho = nodo;
        else derecho.insertarNodo(nodo);
    }

    public byte getLlave() {
        return llave;
    }

    public long getDirLogica() {
        return dirLogica;
    }

}
