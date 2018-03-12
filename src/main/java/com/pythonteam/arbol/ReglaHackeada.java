package com.pythonteam.arbol;

public class ReglaHackeada
{

    public static int CANTIDAD_REGISTROS = 5;
    public static int TAM_REGISTRO = 20;

    private int llave;
    private String reglas;
    private String consecuente;

    public ReglaHackeada() {
    }

    public ReglaHackeada(int llave, String reglas, String consecuente) {
        this.llave = llave;
        this.reglas = reglas;
        this.consecuente = consecuente;
    }

    public int getLlave() {
        return llave;
    }

    public void setLlave(int llave) {
        this.llave = llave;
    }

    public String getReglas() {
        return reglas;
    }

    public void setReglas(String reglas) {
        this.reglas = reglas;
    }

    public String getConsecuente() {
        return consecuente;
    }

    public void setConsecuente(String consecuente) {
        this.consecuente = consecuente;
    }

}
