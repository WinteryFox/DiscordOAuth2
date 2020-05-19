package com.discordoauth2;

import com.discordoauth2.payload.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.rest.util.RouteUtils;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class OAuth2Provider {
    private final String BASE_URI = "https://discord.com/api/oauth2";
    private final String AUTHORIZATION_URI = "/authorize";
    private final String TOKEN_URI = "/token";
    private final String REVOKE_URI = "/revoke";

    private final HttpClient client = HttpClient.create().baseUrl(BASE_URI);

    private final OAuth2Configuration configuration;

    private final ObjectMapper mapper = new ObjectMapper();

    public OAuth2Provider(OAuth2Configuration configuration) {
        this.configuration = configuration;
    }

    public OAuth2Configuration getConfiguration() {
        return configuration;
    }

    public String authorizeUri() {
        return BASE_URI + AUTHORIZATION_URI + "?response_type=code"
                + "&client_id=" + configuration.getClientId().asString()
                + "&scope=" + RouteUtils.encodeUriComponent(configuration.getScopesAsString(), RouteUtils.Type.QUERY)
                + "&redirect_uri=" + RouteUtils.encodeUriComponent(configuration.getRedirectUri(), RouteUtils.Type.PATH_SEGMENT)
                + "&state=" + RouteUtils.encodeUriComponent(configuration.getState(), RouteUtils.Type.QUERY_PARAM);
    }

    public Mono<Token> token(String code, String state) {
        if (!state.equals(configuration.getState()))
            return Mono.error(new IllegalStateException("State returned is not the same as in the configuration, someone might be doing something fishy!"));

        return client.headers(header -> header.add(HttpHeaderNames.CONTENT_TYPE, "application/x-www-form-urlencoded"))
                .post()
                .uri(TOKEN_URI)
                .sendForm((req, form) -> form.attr("client_id", configuration.getClientId().asString())
                        .attr("client_secret", configuration.getClientSecret())
                        .attr("grant_type", "authorization_code")
                        .attr("code", code)
                        .attr("redirect_uri", configuration.getRedirectUri())
                        .attr("scope", configuration.getScopesAsString()))
                .responseSingle((response, buffer) -> buffer.asString().map(b -> Tuples.of(response, b)))
                .flatMap(tuple -> {
                    if (tuple.getT1().status() != HttpResponseStatus.OK)
                        return Mono.error(new RuntimeException(tuple.getT2()));

                    return Mono.fromCallable(() -> mapper.readValue(tuple.getT2(), Token.class));
                });
    }
}