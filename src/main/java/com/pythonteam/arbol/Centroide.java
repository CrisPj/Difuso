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
        ArrayList<Punto> puntitos = new ArrayList<>();
        ArrayList<Funcion> funciones = variable.getFunciones();
        boolean flag = false;
        for (int i = 0; i < funciones.size(); i++) {
            Funcion f = funciones.get(i);
                if (f.getPuntos().size() == 3)
                {
                    if (maximos[i] == 0)
                    {
                        if (f.getPuntos().get(2).getX()==100)
                        {
                            puntitos.add(new Punto(f.getPuntos().get(2).getX(),f.getPuntos().get(2).getY()));
                        }
                        if (f.getPuntos().get(0).getX()==0)
                        {
                            puntitos.add(new Punto(f.getPuntos().get(0).getX(),f.getPuntos().get(0).getY()));
                            flag = true;
                            if (maximos[i+1] == 0)
                            {
                                puntitos.add(new Punto(f.getPuntos().get(2).getX(),f.getPuntos().get(2).getY()));
                                flag = false;
                            }

                        }
                        else {
                            if (flag)
                                puntitos.add(new Punto(f.getPuntos().get(0).getX(), f.getPuntos().get(0).getY()));
                            puntitos.add(new Punto(f.getPuntos().get(2).getX(), f.getPuntos().get(2).getY()));
                        }

                    }
                    else {
                        if (flag || i == 0) {
                            puntitos.add(new Punto(f.getPuntos().get(0).getX(), f.getPuntos().get(0).getY()));
                            flag = false;
                        }
                        puntitos.add(new Punto((f.getPuntos().get(0).getX()+(f.getPuntos().get(1).getX() - f.getPuntos().get(0).getX()) / 2), maximos[i]));
                        puntitos.add(new Punto((f.getPuntos().get(1).getX()+(f.getPuntos().get(2).getX() - f.getPuntos().get(1).getX()) / 2), maximos[i]));
                        if (i == funciones.size()-1)
                        {
                            puntitos.add(new Punto(f.getPuntos().get(2).getX(), f.getPuntos().get(2).getY()));
                        }
                        else {
                            if (maximos[i+1] != 0)
                            {
                                Funcion f2 =funciones.get(i+1);
                                Double x1a = f.getPuntos().get(f.getPuntos().size()-2).getX();
                                Double x2b = f2.getPuntos().get(1).getX();
                                Double puntoM = (x1a+x2b)/2;
                                Double puntoMy = maximos[i+1];
                                puntitos.add(new Punto(puntoM, puntoMy));
                            }else {
                                puntitos.add(new Punto(f.getPuntos().get(2).getX(), f.getPuntos().get(2).getY()));
                            }

                        }

                    }
                }
                else {
                    if (maximos[i] == 0)
                    {
                        if (f.getPuntos().get(3).getX()==100)
                        {
                            puntitos.add(new Punto(f.getPuntos().get(3).getX(),f.getPuntos().get(3).getY()));
                        }
                        if (f.getPuntos().get(0).getX()==0)
                        {
                            puntitos.add(new Punto(f.getPuntos().get(0).getX(),f.getPuntos().get(0).getY()));
                            flag=true;
                            if (maximos[i+1] == 0)
                            {
                                puntitos.add(new Punto(f.getPuntos().get(3).getX(),f.getPuntos().get(3).getY()));
                                flag=false;
                            }
                        }
                        else {
                            if (flag)
                                puntitos.add(new Punto(f.getPuntos().get(0).getX(), f.getPuntos().get(0).getY()));
                            puntitos.add(new Punto(f.getPuntos().get(3).getX(), f.getPuntos().get(3).getY()));
                        }
                    }
                    else {
                        if (flag || i == 0)
                        {
                            puntitos.add(new Punto(f.getPuntos().get(0).getX(), f.getPuntos().get(0).getY()));
                            flag = false;
                        }
                        puntitos.add(new Punto(f.getPuntos().get(1).getX(), maximos[i]));
                        puntitos.add(new Punto(f.getPuntos().get(2).getX(), maximos[i]));
                        if (i == funciones.size()-1)
                        {
                            puntitos.add(new Punto(f.getPuntos().get(3).getX(), f.getPuntos().get(3).getY()));
                        }
                        else {
                            if (maximos[i+1] != 0)
                            {
                                Funcion f2 =funciones.get(i+1);
                                Double x1a = f.getPuntos().get(f.getPuntos().size()-2).getX();
                                Double x2b = f2.getPuntos().get(1).getX();
                                Double puntoM = (x1a+x2b)/2;
                                Double puntoMy = maximos[i+1];
                                puntitos.add(new Punto(puntoM, puntoMy));
                            }else {
                                puntitos.add(new Punto(f.getPuntos().get(3).getX(), f.getPuntos().get(3).getY()));
                            }

                        }

                    }
                }

        }

        double sum1 = 0;
        double sum2 = 0;
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

