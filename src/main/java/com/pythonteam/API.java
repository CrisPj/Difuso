package com.pythonteam;

import com.pythonteam.arbol.*;
import com.pythonteam.archivos.ArchivoReglas;
import com.pythonteam.archivos.ArchivoMaestro;
import com.pythonteam.common.Constantes;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public class API {

    private ArchivoMaestro archivoMaestro;
    private ArchivoReglas archivoReglas;

    public API() {
        archivoMaestro = new ArchivoMaestro(Constantes.NOMBRE_ARCHIVOS, Constantes.LECTURA_ESCRITURA);
        archivoMaestro.generarArbol();
        archivoReglas = new ArchivoReglas(Constantes.NOMBRE_ARCHIVOS, Constantes.LECTURA_ESCRITURA);
        try {
            if (archivoReglas.isEmpty())
            {
                genRules();
            }
        } catch (IOException e) {
            System.out.println("no se puede crear las reglas");
        }
    }

    public void addVar(JsonObject bodyAsJson) throws Exception {
        
        Variable var = new Variable();
        int size = archivoMaestro.imprimirReglas().size();
        int id = 0;
        if (size != 0)
            id = archivoMaestro.imprimirReglas().get(size-1).getId()+1;
        var.setId(id);
        String nombre = bodyAsJson.getString("nombre");
        if (nombre.isEmpty())
            throw new Exception();
        var.setNombre(nombre);
        var.setAlias(bodyAsJson.getString("alias"));
        if (bodyAsJson.containsKey("salida"))
        var.setSalida(bodyAsJson.getBoolean("salida"));
        JsonArray al = bodyAsJson.getJsonArray("funciones");
        ArrayList<Funcion> funciones = new ArrayList<>();
        for (int i = 0; i < al.size(); i++) {
            funciones.add(al.getJsonObject(i).mapTo(Funcion.class));
        }
        var.setFunciones(funciones);
        archivoMaestro.nuevoRegistro(var);
    }

    public double inferencia(JsonObject body) throws Exception {
        JsonArray json = body.getJsonArray("valores");

        ArrayList<Double> entradas = new ArrayList<>();

        for (int i = 0; i < json.size(); i++) {
            entradas.add(Double.valueOf(json.getJsonObject(i).getString("valor")));
        }

        ArrayList<Variable> listaVariables = archivoMaestro.imprimirReglas();
        //InferenciaDifusa inferencia = new InferenciaDifusa(listaVariables, entradas);
       //return inferencia.calcularSalida().getValorDifuso();
        return 0.0;
    }

    public Regla getRule(int id){
        return archivoReglas.obtenerRegla(id);
    }

    public void updateRule(JsonObject body) {
        archivoReglas.editarRegla(body.mapTo(Regla.class));
    }

    public ArrayList getAllVars() {
        return archivoMaestro.imprimirReglas();
    }

    public void rmVar(JsonObject bodyAsJson) {
        archivoMaestro.eliminarRegla(bodyAsJson.getInteger("id"));
    }

    public void updateVar(JsonObject bodyAsJson) {
        archivoMaestro.editarRegla(bodyAsJson.mapTo(Variable.class));
    }

    public Variable getVar(int id) {
        return archivoMaestro.obtenerRegla(id);
    }

    public ArrayList<Regla> genRules() {
        int id = 0;
        ArrayList<Elemento> elementos;
        ArrayList<ArrayList> elchido = new ArrayList<>();

        ArrayList<Variable> variables = archivoMaestro.imprimirReglas();

        for (Variable var : variables) {
            elementos = new ArrayList<>();
            if (!var.isSalida()) {
                for (Funcion fun : var.getFunciones()) {
                    Elemento ele = new Elemento();
                    ele.setAlias(var.getAlias());
                    ele.setFuncion(fun.getNombre());
                    elementos.add(ele);
                }
                elchido.add(elementos);
            }
        }

        int contadores[] = new int[elchido.size()];

        int numReglas = 1;
        for (int i = 0; i < contadores.length; i++) {
            contadores[i] = elchido.get(i).size();
            numReglas *= (contadores[i]);
        }
        int contadores3[] = new int[contadores.length];
        System.arraycopy(contadores,0,contadores3,0,contadores.length);
        for (int i = numReglas; i > 0; i--) {
            ArrayList<Elemento> auxElementos = new ArrayList<>();
            for (int j = 0; j < contadores.length; j++) {
                auxElementos.add((Elemento) elchido.get(j).get(contadores3[j]-1));
            }
            Regla auxR = new Regla();
            auxR.setAntecedentes(auxElementos);
            auxR.setId(id);
            id++;
            archivoReglas.insertarRegla(auxR);
            for (int j = contadores.length-1; j >=0 ; j--)
            {
                if (contadores3[j] == 1) {
                    contadores3[j] = contadores[j];
                }
                else {
                    contadores3[j]--;
                    break;
                }
            }
        }
        return archivoReglas.obtenerReglas();
    }

    public ArrayList<Regla> getAllRules() {
        return archivoReglas.obtenerReglas();
    }
}
