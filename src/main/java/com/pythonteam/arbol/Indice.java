package com.pythonteam.arbol;

public class Indice {
    private int llave;
    private long direccion;

    public Indice(int llave, long direccion)
    {
        this.llave = llave;
        this.direccion = direccion;
    }

    public int getLlave() {
        return llave;
    }

    public void setLlave(int llave) {
        this.llave = llave;
    }

    public long getDireccion() {
        return direccion;
    }

    public void setDireccion(long direccion) {
        this.direccion = direccion;
    }
}
