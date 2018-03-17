package com.pythonteam.arbol;

import java.util.ArrayList;

public class Variable {
    private int id;
    private String nombre;
    private String alias;
    private ArrayList<Funcion> funciones;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Funcion> getFunciones() {
        return funciones;
    }

    public void setFunciones(ArrayList<Funcion> funciones) {
        this.funciones = funciones;
        genPuntos();
    }

    private void genPuntos() {
        for (int i = 0; i < funciones.size(); i++) {
            ArrayList<Punto> puntos = new ArrayList<>();
            if (i == 0) {
                //si es un triangulo y es el primer punto
                if (funciones.get(i).getPuntoCritico().length == 1) {
                    puntos.add(new Punto(0, 0));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0], 1));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0] * 2, 0));
                    funciones.get(i).setPuntos(puntos);
                } else if (funciones.get(i).getPuntoCritico().length == 2) {
                    puntos.add(new Punto(0, 0));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0], 1));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[1], 1));
                    puntos.add(new Punto(((funciones.get(i).getPuntoCritico()[0] + funciones.get(i).getPuntoCritico()[1]) / 2) + funciones.get(i).getPuntoCritico()[1], 1));
                }
            } else {
                double d;
                if (funciones.get(i).getPuntoCritico().length == 1) {
                    d = getD(funciones, i);
                    new Punto(funciones.get(i).getPuntoCritico()[0], 1);
                    new Punto(funciones.get(i).getPuntoCritico()[0] + d, 0);
                } else if (funciones.get(i).getPuntoCritico().length == 1) {
                    d = getD(funciones, i);
                    new Punto(funciones.get(i).getPuntoCritico()[0], 1);
                    new Punto(funciones.get(i).getPuntoCritico()[1], 1);
                    new Punto(funciones.get(i).getPuntoCritico()[1] + d, 0);
                }
            }
            funciones.get(i).setPuntos(puntos);
        }
    }

    private double getD(ArrayList<Funcion> funciones, int i) {
        double d;
        if (funciones.get(i - 1).getPuntoCritico().length == 1) {
            d = funciones.get(i - 1).getPuntoCritico()[0] - funciones.get(i).getPuntoCritico()[0] * funciones.get(i).getTranslape();
            new Punto(funciones.get(i).getPuntoCritico()[0] - d, 0);
        } else {
            d = funciones.get(i - 1).getPuntoCritico()[1] - funciones.get(i).getPuntoCritico()[0] * funciones.get(i).getTranslape();
            new Punto(d, 0);
        }
        return d;
    }
}
