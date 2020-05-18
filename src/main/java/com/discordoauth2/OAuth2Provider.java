package com.discordoauth2;

import discord4j.rest.util.RouteUtils;
import io.netty.handler.codec.http.HttpHeaderNames;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public class OAuth2Provider {
    private final String BASE_URI = "https://discord.com/api/oauth2";
    private final String AUTHORIZATION_URI = "/authorize";
    private final String TOKEN_URI = "/token";
    private final String REVOKE_URI = "/revoke";

    private final HttpClient client = HttpClient.create().baseUrl(BASE_URI);

    private final OAuth2Configuration configuration;

    public OAuth2Provider(OAuth2Configuration configuration) {
        this.configuration = configuration;
    }

    public OAuth2Configuration getConfiguration() {
        return configuration;
    }

    public String authorizeUri() {
        return BASE_URI + AUTHORIZATION_URI + "?response_type=code"
                + "&client_id=" + configuration.getClientId().asString()
                + "&scope=" + configuration.getScopes().asString()
                + "&redirect_uri=" + RouteUtils.encodeUriComponent(configuration.getRedirectUri(), RouteUtils.Type.PATH_SEGMENT)
                + "&state=" + configuration.getState();
    }

    public String tokenUri() {
        return BASE_URI + TOKEN_URI;
    }

    public Mono<String> token(String code) {
        return HttpClient.create()
                .headers(header -> header.add(HttpHeaderNames.CONTENT_TYPE, "application/x-www-form-urlencoded"))
                .post()
                .uri(tokenUri())
                .sendForm((req, form) -> {
                    form.attr("client_id", configuration.getClientId().asString())
                            .attr("client_secret", configuration.getClientSecret())
                            .attr("grant_type", "authorization_code")
                            .attr("code", code)
                            .attr("redirect_uri", configuration.getRedirectUri())
                            .attr("scope", configuration.getScopes().asString());
                })
                .responseContent()
                .aggregate()
                .asString();
    }

    public String revokeUri() {
        return BASE_URI + REVOKE_URI;
    }
}