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
        for (int i = 0; i < funciones.size(); i++) {
            Funcion f = funciones.get(i);
            for (Punto p : f.getPuntos()) {
                if (p.getY() == 1)
                    puntitos.add(new Punto(p.getX(), maximos[i]));
                else
                    puntitos.add(new Punto(p.getX(), p.getY()));
            }
        }
        int i = 0;
        while (i < puntitos.size())
        {
            if(puntitos.get(i).getY() == 0 && i != 0 && i != puntitos.size())
            {
                puntitos.remove(i);
            }
            else
            i++;
        }

        double sum1 = 0;
        double sum2 = 0;
        for (int j = 0; j <puntitos.size()-1; j++) {
            for (double k = puntitos.get(j).getX(); k < puntitos.get(j+1).getX(); k++) {
                sum1 += puntitos.get(j).getY();
                sum2 += puntitos.get(j).getY() * k;
            }
        }
        if (sum1 == 0)
            sum1=1;
        return new Resultado(puntitos,sum2/sum1);
    }

}

