package com.discordoauth2;

import discord4j.rest.util.Snowflake;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import reactor.netty.http.server.HttpServer;

public class Test {
    public static void main(String[] args) {
        final OAuth2Provider provider = new OAuth2Provider(new OAuth2Configuration.OAuth2ConfigurationBuilder(
                Snowflake.of(System.getenv("client_id")),
                System.getenv("secret"),
                ScopeSet.of(Scope.IDENTIFY, Scope.EMAIL),
                "http://localhost/callback")
                .build());
        System.out.println(provider.authorizeUri());

        HttpServer.create()
                .port(80)
                .route(routes -> {
                    routes.get("/",
                            (request, response) -> response.status(HttpResponseStatus.SEE_OTHER).header("Location", provider.authorizeUri()));
                    routes.get("/callback",
                            (request, response) -> response.sendString(provider.token(new QueryStringDecoder(request.uri()).parameters().get("code").get(0))));
                })
                .bindNow()
                .onDispose()
                .block();
    }
}