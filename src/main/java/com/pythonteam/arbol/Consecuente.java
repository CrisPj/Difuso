package com.pythonteam.arbol;

public class Consecuente {
    private String alias;
    private Double valorDifuso;

    public Consecuente(String alias, Double valorDifuso) {
        this.alias = alias;
        this.valorDifuso = valorDifuso;
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
}
