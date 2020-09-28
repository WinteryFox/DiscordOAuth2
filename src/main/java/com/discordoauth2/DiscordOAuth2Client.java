package com.discordoauth2;

import com.discordoauth2.payload.Token;
import reactor.core.publisher.Mono;

class DiscordOAuth2Client {
    private final RestClient client;
    private final String token;
    private final String refreshToken;

    public DiscordOAuth2Client(String token, String refreshToken) {
        this.client = new RestClient(token);
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public DiscordOAuth2Client(Token token) {
        this(token.getAccessToken(), token.getRefreshToken());
    }

    public Mono<User> getUser() {
        return client.request(Routes.SELF_GET, User.class);
    }
}
