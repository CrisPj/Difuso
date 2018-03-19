package com.pythonteam.arbol;

import java.util.ArrayList;

public class Funcion {
    private String nombre;
    private double puntoCritico[];
    private int translape;
    private ArrayList<Punto> puntos;
    private double valorDifuso;

    public double calcMembresia(double x) {
        ArrayList<Punto> puntos = getPuntos();
        Punto a, b;
        a = puntos.get(0);
        for (int i = 1; i < puntos.size(); i++) {
            b = puntos.get(i);
            if (a.getX() <= x && b.getX() >= x) {
                return f(x, a, b);
            }
            a = b;
            b = null;
        }
        return 0;
    }

    /*
     *  y = mx + B
     */
    public double f(double x, Punto a, Punto b) {
        double m = calcM(a, b);
        double B = calcB(a, m);
        return (m * x + B);
    }


    /*
     *  x = (y-B)/m
     */
    public double inversa(double y, Punto a, Punto b) {
        double m = calcM(a, b);
        double B = calcB(a, m);
        return (y - B) / m;
    }


    public double calcM(Punto a, Punto b) {
        return (b.getY() - a.getY()) / (b.getX() - a.getX());
    }
    public double calcB(Punto x, double m){
        return x.getY() - (m*x.getX());
    }

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

    public ArrayList<Punto> getPuntos() {
        return puntos;
    }

    public void setPuntos(ArrayList<Punto> puntos) {
        this.puntos = puntos;
    }

    public double getValorDifuso() {
        return valorDifuso;
    }

    public void setValorDifuso(double valorDifuso) {
        this.valorDifuso = valorDifuso;
    }
}
