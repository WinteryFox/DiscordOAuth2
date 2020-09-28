package com.discordoauth2;

import com.discordoauth2.payload.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.function.Tuples;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OAuth2Provider {
    private final HttpClient client = HttpClient.create().baseUrl(Routes.BASE_URL);
    private final OAuth2Configuration configuration;
    private final ObjectMapper mapper = new ObjectMapper();

    public OAuth2Provider(OAuth2Configuration configuration) {
        this.configuration = configuration;
    }

    public OAuth2Configuration getConfiguration() {
        return configuration;
    }

    public String authorizeUri() {
        return Routes.BASE_URL + Routes.AUTHORIZE.getUri() + "?response_type=code"
                + "&client_id=" + configuration.getClientId()
                + "&scope=" + URLEncoder.encode(configuration.getScopesAsString(), StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(configuration.getRedirectUri(), StandardCharsets.UTF_8)
                + "&state=" + URLEncoder.encode(configuration.getState(), StandardCharsets.UTF_8);
    }

    public Mono<Token> token(String code, String state) {
        if (!state.equals(configuration.getState()))
            return Mono.error(new IllegalStateException("State returned is not the same as in the configuration, someone might be doing something fishy!"));

        return client.headers(header -> header.add(HttpHeaderNames.CONTENT_TYPE, "application/x-www-form-urlencoded"))
                .post()
                .uri(Routes.TOKEN.getUri())
                .sendForm((req, form) -> form.attr("client_id", configuration.getClientId().toString())
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