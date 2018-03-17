package com.pythonteam;

import com.pythonteam.arbol.Funcion;
import com.pythonteam.arbol.Variable;
import com.pythonteam.archivos.ArchivoMaestro;
import com.pythonteam.common.Constantes;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.ArrayList;

public class Main {




    static API api;
    public static void main(String[] args) {
        Funcion f = new Funcion();
        f.setNombre("malo");
        f.setTranslape(20);
        f.setPuntoCritico(new int[]{1});
        ArrayList<Funcion> fs = new ArrayList<>();
        fs.add(f);
        Variable v = new Variable();

        v.setNombre("asd");
        v.setAlias("a");
        v.setId(0);
        v.setFunciones(fs);

        ArchivoMaestro archivoMaestro = new ArchivoMaestro(Constantes.NOMBRE_ARCHIVOS, Constantes.LECTURA_ESCRITURA);
        archivoMaestro.eliminarTodo();
        archivoMaestro.nuevoRegistro(v);
        /*
        api = new API();
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                .allowedHeader("Access-Control-Request-Method")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Content-Type"));

        router.route().handler(BodyHandler.create());

        router.route("/").handler(routingContext -> routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily("Version:EAP"))
        );

        router.route("/addVar").handler(Main::addVar);

        router.route("/getVars").handler(routingContext -> routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(api.getAllVars())));

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
                */
    }

    private static void addVar(RoutingContext routingContext) {
        try {
            api.addVar(routingContext.getBodyAsJson());
            routingContext.response()
                    .setStatusCode(201)
                    .end(Json.encodePrettily(api.getAllVars()));
        } catch (Exception e) {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .setStatusCode(404).end("{\"error\":\"Campos no deben ir vacios\"}");
        }

    }
}
