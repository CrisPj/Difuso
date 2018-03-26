package com.pythonteam.arbol;

import java.util.ArrayList;

public class InferenciaDifusa
{
    private ArrayList<Variable> listaVariables;
    private ArrayList<Double> entradas;

    public Double resultadoInferencia;

    public InferenciaDifusa(ArrayList<Variable> listaVariables, ArrayList<Double> entradas) {
        this.listaVariables = listaVariables;
        this.entradas = entradas;
    }


    public void calcularSalida() throws Exception {
        Double membresia;
        int indice = 0;

        if (listaVariables.size() < 3 && listaVariables.stream().anyMatch(Variable::isSalida))
            throw new Exception("Se necesitan mas variables");

        for (Variable variable : listaVariables)
        {
            if (!variable.isSalida())
            {
                Double variableEntradaActual = entradas.get(indice);

                for (Funcion fs : variable.getFunciones())
                {
                    membresia = fs.calcMembresia(variableEntradaActual);
                    fs.setValorDifuso(membresia);
                    System.out.println("El valor de membresia en Variable: " + variable.getNombre()+ " en funcion:  "
                            +  fs.getNombre() + " es de " + membresia);
                }
                indice++;
            }
        }

    }
}
