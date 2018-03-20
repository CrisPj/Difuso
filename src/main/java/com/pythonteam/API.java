package com.pythonteam;

import com.pythonteam.arbol.Funcion;
import com.pythonteam.arbol.InferenciaDifusa;
import com.pythonteam.arbol.Variable;
import com.pythonteam.archivos.ArchivoReglas;
import com.pythonteam.archivos.ArchivoMaestro;
import com.pythonteam.common.Constantes;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class API {

    private ArchivoMaestro archivoMaestro;
    private ArchivoReglas archivoReglas;

    public API() {
        archivoMaestro = new ArchivoMaestro(Constantes.NOMBRE_ARCHIVOS, Constantes.LECTURA_ESCRITURA);
        archivoMaestro.generarArbol();
        archivoReglas = new ArchivoReglas(Constantes.NOMBRE_ARCHIVOS, Constantes.LECTURA_ESCRITURA);
        // Se borran los hechos al inicio del programa:
        archivoReglas.borrarReglas();
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
        JsonArray al = bodyAsJson.getJsonArray("funciones");
        ArrayList<Funcion> funciones = new ArrayList<>();
        for (int i = 0; i < al.size(); i++) {
            funciones.add(al.getJsonObject(i).mapTo(Funcion.class));
        }
        var.setFunciones(funciones);
        archivoMaestro.nuevoRegistro(var);
    }

    public Funcion inferencia(JsonObject body)
    {
        ArrayList entradas = Json.decodeValue(body.getString("entradas"), ArrayList.class);
        ArrayList<Variable> listaVariables = archivoMaestro.imprimirReglas();
        InferenciaDifusa inferencia = new InferenciaDifusa(listaVariables, entradas);
        return inferencia.calcularSalida();
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
}
