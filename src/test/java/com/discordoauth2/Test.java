package com.discordoauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.rest.util.Snowflake;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        final OAuth2Provider provider = new OAuth2Provider(new OAuth2Configuration.OAuth2ConfigurationBuilder(
                Snowflake.of(System.getenv("client_id")),
                System.getenv("secret"),
                new HashSet<>(Stream.of(Scope.IDENTIFY, Scope.GUILDS).collect(Collectors.toSet())),
                //new HashSet<>(Arrays.stream(Scope.values()).filter(scope -> !scope.isWhitelist()).collect(Collectors.toSet())),
                "http://localhost/callback")
                .build());

        ObjectMapper mapper = new ObjectMapper();

        HttpServer.create()
                .port(80)
                .route(routes -> {
                    routes.get("/",
                            (request, response) -> response.status(HttpResponseStatus.SEE_OTHER)
                                    .header("Location", provider.authorizeUri()));
                    routes.get("/callback",
                            (request, response) -> {
                                QueryStringDecoder decoder = new QueryStringDecoder(request.uri());

                                if (decoder.parameters().containsKey("error"))
                                    return Mono.error(new IllegalStateException(decoder.parameters().get("error").get(0)
                                            + ": " + decoder.parameters().get("error_description").get(0)));

                                return provider.token(
                                        decoder.parameters().get("code").get(0),
                                        decoder.parameters().get("state").get(0))
                                        .map(DiscordOAuth2Client::new)
                                        .flatMap(DiscordOAuth2Client::getUser)
                                        .flatMap(user -> response.header(HttpHeaderNames.CONTENT_TYPE, "application/json")
                                                .sendString(Mono.just(user)).then());
                            });
                })
                .bindNow()
                .onDispose()
                .block();
    }
}