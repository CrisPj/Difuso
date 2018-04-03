package com.pythonteam.arbol;

import java.util.ArrayList;

public class Centroide
{
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
                    if (maximos[i] != f.getPuntos().get(1).getY())
                        puntitos.add(new Punto(f.getPuntos().get(1).getX() + (f.getPuntos().get(1).getX()-chido), maximos[i]));
                    if (f.getPuntos().get(2).getY() != 0)
                        puntitos.add(new Punto(f.getPuntos().get(2).getX(), f.getPuntos().get(2).getY()));
                    else
                        puntitos.add(new Punto(f.getPuntos().get(2).getX(), 0));


                }
                else if (f.getPuntos().size() == 4) {
                    puntitos.add(new Punto(f.getPuntos().get(0).getX(), f.getPuntos().get(0).getY()));
                    puntitos.add(new Punto(f.getPuntos().get(1).getX(), maximos[i]));
                    puntitos.add(new Punto(f.getPuntos().get(2).getX(), maximos[i]));
                    if ( f.getPuntos().get(3).getY()  > maximos[i])
                        puntitos.add(new Punto(f.getPuntos().get(3).getX(), maximos[i]));
                    else if (f.getPuntos().get(3).getY() != 0)
                        puntitos.add(new Punto(f.getPuntos().get(3).getX(), f.getPuntos().get(3).getY() ));
                    else
                        puntitos.add(new Punto(f.getPuntos().get(3).getX(), 0));
                }
            }
        }




        for (int j = 0; j <puntitos.size()-1; j++) {
            for (double k = puntitos.get(j).getX(); k < puntitos.get(j+1).getX(); k++) {
                if (puntitos.get(j).getY() != puntitos.get(j+1).getY())
                {
                    double difX = puntitos.get(j+1).getX() - puntitos.get(j).getX();
                    double dify = Math.abs(puntitos.get(j+1).getY() - puntitos.get(j).getY());
                    double calc = dify / difX;
                    sum1 += calc;
                    sum2  = sum2 + (calc * k);
                }
                else
                {
                    sum1 += puntitos.get(j).getY();
                    sum2  = sum2 + (puntitos.get(j).getY() * k);
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

