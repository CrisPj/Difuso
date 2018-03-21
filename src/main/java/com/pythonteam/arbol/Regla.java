package com.pythonteam.arbol;

import java.util.ArrayList;

public class Regla {
    private int id;
    private ArrayList<Elemento> antecedentes;
    private Elemento consecuente;

    public ArrayList<Elemento> getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(ArrayList<Elemento> antecedentes) {
        this.antecedentes = antecedentes;
    }

    public Elemento getConsecuente() {
        return consecuente;
    }

    public void setConsecuente(Elemento consecuente) {
        this.consecuente = consecuente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
