package com.pythonteam;

import com.pythonteam.arbol.Centroide;
import com.pythonteam.arbol.Elemento;
import com.pythonteam.arbol.Funcion;
import com.pythonteam.arbol.InferenciaDifusa;
import com.pythonteam.arbol.Regla;
import com.pythonteam.arbol.Resultado;
import com.pythonteam.arbol.Variable;
import com.pythonteam.archivos.ArchivoReglas;
import com.pythonteam.archivos.ArchivoMaestro;
import com.pythonteam.common.Constantes;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;

public class API {

    private ArchivoMaestro archivoMaestro;
    private ArchivoReglas archivoReglas;

    public API() {
        archivoMaestro = new ArchivoMaestro(Constantes.NOMBRE_ARCHIVOS, Constantes.LECTURA_ESCRITURA);
        archivoReglas = new ArchivoReglas(Constantes.NOMBRE_ARCHIVOS, Constantes.LECTURA_ESCRITURA);
        try {
            genRules();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addVar(JsonObject bodyAsJson) throws Exception {
        
        Variable var = new Variable();
        int tam = archivoMaestro.tamanio();
        int id = 0;
        if (tam != 0)
            id = archivoMaestro.obtenerRegla(tam-1).getId()+1;
        var.setId(id);
        String nombre = bodyAsJson.getString("nombre");
        if (nombre.isEmpty())
            throw new Exception("hace falta campo nombre");
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
        genRules();
    }

    public Resultado inferencia(JsonObject body) throws Exception {

        JsonArray json = body.getJsonArray("valores");

        ArrayList<Double> entradas = new ArrayList<>();

        for (int i = 0; i < json.size(); i++) {
            entradas.add(Double.valueOf(json.getJsonObject(i).getString("valor")));
        }

        ArrayList<Variable> listaVariables = archivoMaestro.imprimirReglas();
        InferenciaDifusa inferencia = new InferenciaDifusa(listaVariables, entradas);
        inferencia.calcularSalida();
        genRules();
        archivoReglas.obtenerReglas();

        ArrayList<Elemento>  consecuentes = new ArrayList<>();
        for (Regla r : archivoReglas.obtenerReglas()) {
            double min = 1.0;
            for (Elemento e : r.getAntecedentes()) {
                if (e.getValorDifuso() < min)
                    min = e.getValorDifuso();
            }
            r.getConsecuente().setValorDifuso(min);
            consecuentes.add(r.getConsecuente());
        }

        Variable salida = listaVariables.stream().filter(Variable::isSalida).findFirst().orElse(null);
        if (salida == null)
            throw new Exception("No hay salida");

        double valores[] = new double[salida.getFunciones().size()];
        for (int i = 0; i < salida.getFunciones().size(); i++) {
            double maximo = 0;

            for (Elemento c:consecuentes) {
                if (c.getIdFuncion() == i)
                {
                    if (c.getValorDifuso() > maximo)
                        maximo = c.getValorDifuso();
                }
            }
            //System.out.println("El valor maximo en salida  " + salida.getNombre() + " en funcion:  "
            //        + salida.getFunciones().get(i).getNombre() + " es de " + maximo);

            valores[i] = maximo;
        }

        Centroide c = new Centroide(salida,valores);

        return c.genArea();
    }

    public Regla getRule(int id){
        return archivoReglas.obtenerRegla(id);
    }

    public ArrayList getAllVars() {
        return archivoMaestro.imprimirReglas();
    }

    public void rmVar(JsonObject bodyAsJson) throws Exception {
        archivoMaestro.eliminarRegla(bodyAsJson.getInteger("id"));
        genRules();
    }

    public void updateVar(JsonObject bodyAsJson) throws Exception {
        archivoMaestro.editarRegla(bodyAsJson.mapTo(Variable.class));
        genRules();
    }

    public Variable getVar(int id) {
        return archivoMaestro.obtenerRegla(id);
    }

    public void genRules() throws Exception {
        archivoReglas.borrarReglas();
        int id = 0;
        ArrayList<Elemento> elementos;
        ArrayList<ArrayList> elchido = new ArrayList<>();

        ArrayList<Variable> variables = archivoMaestro.imprimirReglas();

        Variable salida;
        salida = variables.stream().filter(Variable::isSalida).findFirst().orElse(null);


        if (salida == null)
            return;
            //throw new Exception("No hay varibles de salida");

        double maxs[] = new double[salida.getFunciones().size()];

        ArrayList<Funcion> funciones = salida.getFunciones();
        for (int i = 0, funcionesSize = funciones.size(); i < funcionesSize; i++) {
            Funcion f =funciones.get(i);
            maxs[i]=f.getPuntos().get(f.getPuntos().size() - 1).getX();
        }

        for (Variable var : variables) {
            elementos = new ArrayList<>();
            if (!var.isSalida()) {
                ArrayList<Funcion> funciones1 = var.getFunciones();
                for (int i = 0, funciones1Size = funciones1.size(); i < funciones1Size; i++) {
                    Funcion fun = funciones1.get(i);
                    Elemento ele = new Elemento();
                    ele.setIdAlias(var.getId());
                    ele.setAlias(var.getAlias());
                    ele.setIdFuncion(i);
                    ele.setFuncion(fun.getNombre());
                    ele.setValorDifuso(fun.getValorDifuso());
                    elementos.add(ele);
                }
                elchido.add(elementos);
            }
        }

        int contadores[] = new int[elchido.size()];

        int numReglas;
        numReglas = elchido.size() > 0 ? 1 : 0;
        if (numReglas != 0)
        {
            for (int i = 0; i < contadores.length; i++) {
                contadores[i] = elchido.get(i).size();
                numReglas *= (contadores[i]);
            }
            int contadores3[] = new int[contadores.length];
            System.arraycopy(contadores,0,contadores3,0,contadores.length);
            for (int i = numReglas; i > 0; i--) {
                ArrayList<Elemento> auxElementos = new ArrayList<>();
                double maxs2 = 0;
                for (int j = 0; j < contadores.length; j++) {
                    Elemento ele = (Elemento) elchido.get(j).get(contadores3[j]-1);
                    auxElementos.add(ele);
                    double h = variables.get(ele.getIdAlias()).getFunciones().get(ele.getIdFuncion()).getPuntos().get(variables.get(ele.getIdAlias()).getFunciones().get(ele.getIdFuncion()).getPuntos().size()-1).getX();
                    h = h - (salida.getFunciones().size() * (salida.getFunciones().size()));
                        maxs2 += h;
                }

                maxs2 = maxs2 / contadores.length;
                if (maxs2 > 100)
                    maxs2 = 100;
                Regla auxR = new Regla();
                auxR.setAntecedentes(auxElementos);
                auxR.setId(id);
                Elemento consecuente = new Elemento();
                consecuente.setValorDifuso(0.0);
                consecuente.setIdAlias(salida.getId());
                consecuente.setAlias(salida.getAlias());

                int funcion = -1;
                for (int z = 0; z <= contadores.length; z++) {
                    if (maxs2 <= maxs[z]) {
                        funcion = z;
                        break;
                    }
                }
                if (funcion == -1)
                    throw new Exception("Error desconocido");

                consecuente.setFuncion(salida.getFunciones().get(funcion).getNombre());
                consecuente.setIdFuncion(funcion);
                auxR.setConsecuente(consecuente);
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
        }
    }

    public ArrayList<Regla> getAllRules() {
        return archivoReglas.obtenerReglas();
    }
}
