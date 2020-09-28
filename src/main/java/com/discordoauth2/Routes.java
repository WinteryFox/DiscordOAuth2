package com.discordoauth2;

public abstract class Routes {
    public static final String BASE_URL = "https://discord.com/api/v8";

    public static final Route AUTHORIZE = Route.get("/oauth2/authorize");

    public static final Route TOKEN = Route.post("/oauth2/token");

    public static final Route REVOKE = Route.get("/oauth2/revoke");

    public static final Route SELF_GET = Route.get("/users/@me");
}
