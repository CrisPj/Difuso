package com.pythonteam.arbol;

public class Funcion {
    private String nombre;
    private double puntoCritico[];
    private int translape;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double[] getPuntoCritico() {
        return puntoCritico;
    }

    public void setPuntoCritico(double[] puntoCritico) {
        this.puntoCritico = puntoCritico;
    }

    public int getTranslape() {
        return translape;
    }

    public void setTranslape(int translape) {
        this.translape = translape;
    }
}
