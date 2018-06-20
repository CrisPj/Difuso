package com.pythonteam;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class Main {
    private static API api;
    public static void main(String[] args) {

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
                .end(Json.encode("Version:EAP"))
        );

        router.route("/addVar").handler(Main::addVar);
        router.route("/inference").handler(Main::inference);

        router.route("/updateVar").handler(Main::updateVar);
        router.route("/rmVar").handler(Main::rmVar);

        router.route("/getVar/:id").handler(Main::getVar);
        router.route("/getRule/:id").handler(Main::getRule);

        router.route("/getVars").handler(routingContext -> routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encode(api.getAllVars())));

        router.route("/getRules").handler(routingContext -> {
            try {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encode(api.getAllRules()));
            } catch (Exception e) {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .setStatusCode(404).end("{\"error\":\""+e.getMessage()+"\"}");
            }
        });

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
    }

    private static void getRule(RoutingContext routingContext) {
        try {
            int id = Integer.parseInt(routingContext.request().getParam("id"));
            routingContext.response()
                    .setStatusCode(201)
                    .end(Json.encode(api.getRule(id)));
        } catch (Exception e) {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .setStatusCode(404).end("{\"error\":\""+e.getMessage()+"\"}");
        }
    }


    private static void inference(RoutingContext routingContext) {
        try {

            routingContext.response()
                    .setStatusCode(201)
                    .end(Json.encode(api.inferencia(routingContext.getBodyAsJson())));
        } catch (Exception e) {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .setStatusCode(404).end("{\"error\":\""+e.getMessage()+"\"}");
        }
    }

    private static void getVar(RoutingContext routingContext) {
        try {
            int id = Integer.parseInt(routingContext.request().getParam("id"));
            routingContext.response()
                    .setStatusCode(201)
                    .end(Json.encode(api.getVar(id)));
        } catch (Exception e) {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .setStatusCode(404).end("{\"error\":\""+e.getMessage()+"\"}");
        }
    }

    private static void updateVar(RoutingContext routingContext) {
        try {
            api.updateVar(routingContext.getBodyAsJson());
            routingContext.response()
                    .setStatusCode(201)
                    .end(Json.encode(api.getAllVars()));
        } catch (Exception e) {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .setStatusCode(404).end("{\"error\":\""+e.getMessage()+"\"}");
        }
    }

    private static void rmVar(RoutingContext routingContext) {
        try {
            api.rmVar(routingContext.getBodyAsJson());
            routingContext.response()
                    .setStatusCode(201)
                    .end(Json.encode(api.getAllVars()));
        } catch (Exception e) {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .setStatusCode(404).end("{\"error\":\""+e.getMessage()+"\"}");
        }
    }

    private static void addVar(RoutingContext routingContext) {
        try {
            api.addVar(routingContext.getBodyAsJson());
            routingContext.response()
                    .setStatusCode(201)
                    .end(Json.encode(api.getAllVars()));
        } catch (Exception e) {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .setStatusCode(404).end("{\"error\":\""+e.getMessage()+"\"}");
        }
    }
}
