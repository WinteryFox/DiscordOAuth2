package com.discordoauth2;

import discord4j.rest.util.Snowflake;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class OAuth2Configuration {
    private final Snowflake clientId;
    private final String clientSecret;
    private final Set<Scope> scopes;
    private final String redirectUri;
    private final String state;

    public OAuth2Configuration(Snowflake clientId, String clientSecret, Set<Scope> scopes, String redirectUri, String state) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scopes = scopes;
        this.redirectUri = redirectUri;
        if (state != null)
            this.state = state;
        else {
            Random random = new Random();
            char[] validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*-+=_".toCharArray();
            char[] bytes = new char[20];
            for (int i = 0; i < bytes.length; i++)
                bytes[i] = validChars[random.nextInt(validChars.length - 1)];
            this.state = new String(bytes);
        }
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Set<Scope> getScopes() {
        return scopes;
    }

    public String getScopesAsString() {
        return scopes.stream().map(Scope::getScope).collect(Collectors.joining(" "));
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public Snowflake getClientId() {
        return clientId;
    }

    public String getState() {
        return state;
    }

    public static class OAuth2ConfigurationBuilder {
        private final Snowflake clientId;
        private final String clientSecret;
        private final Set<Scope> scopes;
        private final String redirectUri;
        private String state;

        public OAuth2ConfigurationBuilder(Snowflake clientId, String clientSecret, Set<Scope> scopes, String redirectUri) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.scopes = scopes;
            this.redirectUri = redirectUri;
        }

        public OAuth2ConfigurationBuilder withState(String state) {
            this.state = state;
            return this;
        }

        public OAuth2Configuration build() {
            return new OAuth2Configuration(clientId, clientSecret, scopes, redirectUri, state);
        }
    }
}
