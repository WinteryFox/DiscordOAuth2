package com.discordoauth2;

import discord4j.rest.util.RouteUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ScopeSet {
    private final Set<Scope> scopes;

    public ScopeSet(Set<Scope> scopes) {
        this.scopes = scopes;
    }

    public Set<Scope> getScopes() {
        return scopes;
    }

    public String asString() {
        return RouteUtils.encodeUriComponent(
                String.join(" ", scopes.parallelStream().map(Scope::getScope).collect(Collectors.toSet())),
                RouteUtils.Type.QUERY_PARAM
        );
    }

    public static ScopeSet of(Scope... scopes) {
        Set<Scope> s = new HashSet<>();
        Collections.addAll(s, scopes);
        return new ScopeSet(s);
    }
}