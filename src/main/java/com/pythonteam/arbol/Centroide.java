package com.pythonteam.arbol;

import java.util.ArrayList;

public class Centroide
{
    private Variable variable;
    private double membresia;
    private int resultado;

    public Centroide(Variable variable, double membresia) {
        this.variable = variable;
        this.membresia = membresia;
        genArea();
    }


    private void genArea() {
        ArrayList<Punto> puntitos = new ArrayList<>();
        for (Funcion f : variable.getFunciones())
        {
            for (Punto p : f.getPuntos())
            {
                if (p.getY() == 1)
                    puntitos.add(new Punto(p.getX(),membresia));
                else
                    puntitos.add(new Punto(p.getX(),p.getY()));
            }
        }

    }


    public double getCentroide() {
        return resultado;
    }
}


