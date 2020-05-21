package com.discordoauth2;

import com.discordoauth2.payload.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.object.entity.Member;
import discord4j.rest.http.ExchangeStrategies;
import discord4j.rest.http.client.ClientRequest;
import discord4j.rest.http.client.ClientResponse;
import discord4j.rest.http.client.DiscordWebClient;
import discord4j.rest.route.Route;
import discord4j.rest.route.Routes;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Collections;

public class RestClient {
    private final DiscordWebClient client;

    public RestClient(String token) {
        this.client = new DiscordWebClient(
                HttpClient.create().baseUrl(Routes.BASE_URL),
                ExchangeStrategies.jackson(new ObjectMapper()),
                token,
                "Bearer",
                Collections.emptyList()
        );
    }

    public Mono<ClientResponse> request(Route route) {
        return client.exchange(new ClientRequest(route.newRequest()));
    }
}
