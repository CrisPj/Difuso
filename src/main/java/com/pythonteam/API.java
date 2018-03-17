package com.pythonteam;

import com.pythonteam.arbol.Variable;
import com.pythonteam.archivos.ArchivoReglas;
import com.pythonteam.archivos.ArchivoMaestro;
import com.pythonteam.common.Constantes;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

public class API {

    private ArchivoMaestro archivoMaestro;
    private ArchivoReglas archivoReglas;
    String nombreArchivo = Constantes.NOMBRE_ARCHIVOS;
    private int contador = 0;

    public API() {
        archivoMaestro = new ArchivoMaestro(nombreArchivo, Constantes.LECTURA_ESCRITURA);
        archivoReglas = new ArchivoReglas(nombreArchivo, Constantes.LECTURA_ESCRITURA);
        // Se borran los hechos al inicio del programa:
        archivoReglas.borrarHechos();
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
        archivoMaestro.nuevoRegistro(var);
    }

    public ArrayList getAllVars() {
        return archivoMaestro.imprimirReglas();
    }
}
