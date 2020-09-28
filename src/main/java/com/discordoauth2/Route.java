package com.discordoauth2;

import io.netty.handler.codec.http.HttpMethod;

public final class Route {
    private final HttpMethod method;
    private final String uri;

    private Route(HttpMethod method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public static Route get(String uri) {
        return new Route(HttpMethod.GET, uri);
    }

    public static Route post(String uri) {
        return new Route(HttpMethod.POST, uri);
    }
}
