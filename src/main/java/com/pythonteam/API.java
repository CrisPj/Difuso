package com.pythonteam;

import com.pythonteam.arbol.Variable;
import com.pythonteam.archivos.ArchivoHechos;
import com.pythonteam.archivos.ArchivoMaestro;
import com.pythonteam.common.Constantes;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

public class API {

    private ArchivoMaestro archivoMaestro;
    private ArchivoHechos archivoHechos;
    String nombreArchivo = Constantes.NOMBRE_ARCHIVOS;
    private ArrayList<Variable> allVars;
    private int contador = 0;

    public API() {
        archivoMaestro = new ArchivoMaestro(nombreArchivo, Constantes.LECTURA_ESCRITURA);
        archivoHechos = new ArchivoHechos(nombreArchivo, Constantes.LECTURA_ESCRITURA);
        // Se borran los hechos al inicio del programa:
        archivoHechos.borrarHechos();
        allVars = new ArrayList<>();
    }

    public void addVar(JsonObject bodyAsJson) {
        Variable var = new Variable();
        var.setId(contador++);
        var.setNombre(bodyAsJson.getString("nombre"));
        var.setAlias(bodyAsJson.getString("alias"));
        allVars.add(var);
    }

    public ArrayList getAllVars() {
        return allVars;
    }
}
