package com.pythonteam.arbol;

public class Elemento {
    private String alias;
    private Double valorDifuso;
    private String funcion;
    private int idAlias;
    private int idFuncion;

    public int getIdAlias() {
        return idAlias;
    }

    public void setIdAlias(int idAlias) {
        this.idAlias = idAlias;
    }

    public int getIdFuncion() {
        return idFuncion;
    }

    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }

    public Elemento(String alias, String funcion, Double valorDifuso) {
        this.alias = alias;
        this.funcion = funcion;
        this.valorDifuso = valorDifuso;
    }

    public Elemento() {
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Double getValorDifuso() {
        return valorDifuso;
    }

    public void setValorDifuso(Double valorDifuso) {
        this.valorDifuso = valorDifuso;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }
}
