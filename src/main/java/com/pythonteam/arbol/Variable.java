package com.pythonteam.arbol;

import java.util.ArrayList;

public class Variable {
    private int id;
    private String nombre;
    private String alias;
    private ArrayList<Funcion> funciones;
    private boolean salida;
    private int traslape;
    private ArrayList<Punto> intersecciones;

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

    public void evaluar(double x)
    {

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
                } else if (funciones.get(i).getPuntoCritico().length == 2) {
                    puntos.add(new Punto(0, 0));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0], 1));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[1], 1));
                    puntos.add(new Punto(((funciones.get(i).getPuntoCritico()[0] + funciones.get(i).getPuntoCritico()[1]) / 2) + funciones.get(i).getPuntoCritico()[1], 1));
                }
            } else {
                double d;
                if (funciones.get(i).getPuntoCritico().length == 1) {
                    d = getD(i, puntos);
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0], 1));
                    double x = funciones.get(i).getPuntoCritico()[0] + d;
                    if (x>100)
                        x=100;
                    puntos.add(new Punto(x, 0));
                } else if (funciones.get(i).getPuntoCritico().length == 2) {
                    d = getD(i, puntos);
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0], 1));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[1], 1));
                    double x = funciones.get(i).getPuntoCritico()[1] + d;
                    if (x > 100)
                        x=100;
                    puntos.add(new Punto(x, 0));
                }
            }
            funciones.get(i).setPuntos(puntos);
        }
    }

    private double getD(int i, ArrayList<Punto> puntos) {
        double d;
        if (funciones.get(i - 1).getPuntoCritico().length == 1) {
            d = (funciones.get(i).getPuntoCritico()[0] - funciones.get(i - 1).getPuntoCritico()[0])/100 * funciones.get(i).getTraslape();
            puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0] - d, 0));
        } else {
            d = (funciones.get(i).getPuntoCritico()[0] - funciones.get(i - 1).getPuntoCritico()[1])/100 * funciones.get(i).getTraslape();
            puntos.add(new Punto(d, 0));
        }
        return d;
    }


    public boolean isSalida() {
        return salida;
    }
    public void calcularIntersecciones() {

        for(int i=0;i<funciones.size()-1;i++)
        {
            Funcion f1 =funciones.get(i);
            Funcion f2 =funciones.get(i+1);

            //Penultimo punto de la funcion 1
            Double x1a = f1.getPuntos().get(f1.getPuntos().size()-2).getX();
            //No importa para la segunda funcion que forma tenga siempre se ocupa el primer y segundo punto
            Double x2b = f2.getPuntos().get(1).getX();
            //CAlculamos el punto medio entre las dos funciones
            //el punto medio tambien es el punto de interseccion entre las dos funciones
            Double puntoM = (x1a+x2b)/2;
            //Calcular el valor de y en el punto medio
            Double puntoMy = f1.calcMembresia(puntoM);
            //Guardar el punto en el arreglo de intersecciones
            intersecciones.add(new Punto(puntoM, puntoMy));
        }

    }

    public void setIntersecciones(ArrayList<Punto> intersecciones) {
        this.intersecciones = intersecciones;
    }

    public ArrayList<Punto> getIntersecciones() {
        return intersecciones;
    }
}
