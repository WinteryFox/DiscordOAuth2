package com.discordoauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public class RestClient {
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public RestClient(String token) {
        this.client = HttpClient.create().headers(headers -> headers.add("Authorization", "Bearer " + token));
    }

    public <T> Mono<T> request(Route route, Class<T> clazz) {
        return client
                .request(route.getMethod())
                .uri(Routes.BASE_URL + route.getUri())
                .responseSingle((httpClientResponse, byteBufMono) -> {
                    if (httpClientResponse.status().code() < 200 || httpClientResponse.status().code() >= 300)
                        return Mono.error(
                                new IllegalStateException("Request returned " + httpClientResponse.status().code() + ": " +
                                        httpClientResponse.status().reasonPhrase()));

                    return byteBufMono.asString()
                            .flatMap(json -> Mono.fromCallable(() -> mapper.readValue(json, clazz)));
                });
    }
}
