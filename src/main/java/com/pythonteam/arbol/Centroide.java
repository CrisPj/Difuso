package com.pythonteam.arbol;

import java.util.ArrayList;

public class Centroide
{
    public final double rectangulito = 0.001;
    private Variable variable;
    private double[] maximos;

    public Centroide(Variable variable, double[] maximos) {
        this.variable = variable;
        this.maximos = maximos;
    }


    public Resultado genArea() {
        double sum1 = 0;
        double sum2 = 0;
        ArrayList<Punto> puntitos = new ArrayList<>();
        ArrayList<Funcion> funciones = variable.getFunciones();
        for (int i = 0; i < funciones.size(); i++) {
            Funcion f = funciones.get(i);

            if (maximos[i] != 0) {
                if (f.getPuntos().size() == 3)
                {
                    puntitos.add(new Punto(f.getPuntos().get(0).getX(), f.getPuntos().get(0).getY()));

                    // ((y-y1)/(y2-y1))(x2-x2) + x1
                    double chido = (
                            (maximos[i] - f.getPuntos().get(0).getY())
                                    / (f.getPuntos().get(1).getY() - f.getPuntos().get(0).getY()))
                            * (f.getPuntos().get(1).getX() - f.getPuntos().get(0).getX()) + f.getPuntos().get(0).getX();

                    puntitos.add(new Punto(chido, maximos[i]));


                    for (double j = f.getPuntos().get(0).getX(); j < chido; j+= rectangulito) {
                        sum1 += f.calcMembresia(j);
                        sum2  = sum2 + (f.calcMembresia(j) * j);
                    }
                    double x =0;
                    if (maximos[i] != f.getPuntos().get(1).getY()){
                        x = f.getPuntos().get(1).getX() + (f.getPuntos().get(1).getX()-chido);
                        puntitos.add(new Punto(x, maximos[i]));
                        for (double j = chido; j < x; j+= rectangulito) {
                            sum1 += f.calcMembresia(j);
                            sum2  = sum2 + (f.calcMembresia(j) * j);
                        }
                        chido = x;
                    }
                    if (f.getPuntos().get(2).getY() != 0) {
                        x = f.getPuntos().get(2).getX();
                        puntitos.add(new Punto(x, f.getPuntos().get(2).getY()));
                    }
                    else
                    {
                        x = f.getPuntos().get(2).getX();
                        puntitos.add(new Punto(x, 0));
                    }

                    for (double j = chido; j < x; j+= rectangulito) {
                        sum1 += f.calcMembresia(j);
                        sum2  = sum2 + (f.calcMembresia(j) * j);
                    }

                }
                else if (f.getPuntos().size() == 4) {
                    puntitos.add(new Punto(f.getPuntos().get(0).getX(), f.getPuntos().get(0).getY()));
                    puntitos.add(new Punto(f.getPuntos().get(1).getX(), maximos[i]));
                    for (double j = f.getPuntos().get(0).getX(); j < f.getPuntos().get(1).getX(); j+= rectangulito) {
                        sum1 += f.calcMembresia(j);
                        sum2  = sum2 + (f.calcMembresia(j) * j);
                    }
                    puntitos.add(new Punto(f.getPuntos().get(2).getX(), maximos[i]));
                    for (double j = f.getPuntos().get(1).getX(); j < f.getPuntos().get(2).getX(); j+= rectangulito) {
                        sum1 += f.calcMembresia(j);
                        sum2  = sum2 + (f.calcMembresia(j) * j);
                    }
                    double x = 0;
                    if ( f.getPuntos().get(3).getY()  > maximos[i])
                        puntitos.add(new Punto(f.getPuntos().get(3).getX(), maximos[i]));
                    else if (f.getPuntos().get(3).getY() != 0)
                        puntitos.add(new Punto(f.getPuntos().get(3).getX(), f.getPuntos().get(3).getY() ));
                    else
                        puntitos.add(new Punto(f.getPuntos().get(3).getX(), 0));

                    for (double j = f.getPuntos().get(2).getX(); j < f.getPuntos().get(3).getX(); j+= rectangulito) {
                        sum1 += f.calcMembresia(j);
                        sum2  = sum2 + (f.calcMembresia(j) * j);
                    }
                }
            }
        }

        if (sum1 == 0)
            sum1=1;
        sum1 = Math.ceil(sum1);
        sum2 = Math.ceil(sum2);
        return new Resultado(puntitos,sum2/sum1);
    }

}

