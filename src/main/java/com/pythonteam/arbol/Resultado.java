package com.pythonteam.arbol;

import java.util.ArrayList;

public class Resultado
{
    private ArrayList<Punto> punto;
    private double resultado;

    public Resultado(ArrayList<Punto> punto, double resultado) {
        this.punto = punto;
        this.resultado = resultado;
    }

    public ArrayList<Punto> getPunto() {
        return punto;
    }

    public void setPunto(ArrayList<Punto> punto) {
        this.punto = punto;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }
}
