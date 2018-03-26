package com.pythonteam.arbol;

import java.util.ArrayList;

public class Variable {
    private int id;
    private String nombre;
    private String alias;
    private ArrayList<Funcion> funciones;
    private boolean salida;
    private int traslape;

    public Variable() {
    }

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
                } else if (funciones.get(i).getPuntoCritico().length == 2) {

                    puntos.add(new Punto(0, 0));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0], 1));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[1], 1));
                    double distancia;
                    if (puntos.get(0).getX() == puntos.get(1).getX())
                    {
                        distancia = funciones.get(i).getPuntoCritico()[1]  + ((funciones.get(i).getPuntoCritico()[1] - funciones.get(i).getPuntoCritico()[0])) * funciones.get(i).getTraslape()/100;
                    }
                    else
                        distancia = funciones.get(i).getPuntoCritico()[1] + (puntos.get(1).getX() - puntos.get(0).getX());

                    puntos.add(new Punto(distancia, 0));
                }
            } else {
                double d;
                if (funciones.get(i).getPuntoCritico().length == 1) {
                    d = getD(i, puntos);
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0], 1));
                    double x = puntos.get(1).getX() + (puntos.get(1).getX() - puntos.get(0).getX());
                    puntos.add(new Punto(x, 0));
                    if (x>100)
                    {
                        Funcion fake = new Funcion();
                        fake.setPuntos(puntos);
                        double y = fake.calcMembresia(100);
                        puntos.remove(2);
                        puntos.add(new Punto(100,y));
                    }

                } else if (funciones.get(i).getPuntoCritico().length == 2) {
                    d = getD(i, puntos);
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[0], 1));
                    puntos.add(new Punto(funciones.get(i).getPuntoCritico()[1], 1));
                    double x = puntos.get(2).getX() + (puntos.get(1).getX() - puntos.get(0).getX());
                    puntos.add(new Punto(x, 0));
                    if (x>100)
                    {
                        Funcion fake = new Funcion();
                        fake.setPuntos(puntos);
                        double y = fake.calcMembresia(100);
                        puntos.remove(3);
                        puntos.add(new Punto(100,y));
                    }
                }
            }
            funciones.get(i).setPuntos(puntos);
        }
    }

    private double getD(int i, ArrayList<Punto> puntos) {
        double d = -1;
            int size2 = funciones.get(i-1).getPuntos().size() ;

            if (size2 == 3)
            {
                double dPuntos =funciones.get(i-1).getPuntos().get(2).getX() - funciones.get(i-1).getPuntos().get(1).getX();
                double traslape = funciones.get(i).getTraslape();
                double porcentaje =traslape/100;
                d=(dPuntos) * (porcentaje);
                puntos.add(new Punto(funciones.get(i-1).getPuntos().get(2).getX() - d, 0));
            }
            else if (size2 == 4)
            {
                d=(funciones.get(i-1).getPuntos().get(3).getX() - funciones.get(i-1).getPuntos().get(2).getX())/100 * funciones.get(i).getTraslape();
                puntos.add(new Punto(funciones.get(i-1).getPuntos().get(3).getX() - d, 0));

            }
        return d;
    }

    public boolean isSalida() {
        return salida;
    }

    public void setSalida(boolean salida) {
        this.salida = salida;
    }
}
