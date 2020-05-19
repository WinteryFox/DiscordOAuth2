package com.discordoauth2;

public enum Scope {
    BOT("bot", false),
    CONNECTIONS("connections", false),
    EMAIL("email", false),
    IDENTIFY("identify", false),
    GUILDS("guilds", false),
    GUILDS_JOIN("guilds.join", false),
    GDM_JOIN("gdm.join", false),
    MESSAGES_READ("messages.read", false),
    RPC("rpc", true),
    RPC_API("rpc.api", true),
    RPC_NOTIFICATIONS_READ("rpc.notifications.read", true),
    WEBHOOK_INCOMING("webhook.incoming", false),
    APPLICATIONS_BUILDS_UPLOAD("applications.builds.upload", true),
    APPLICATIONS_BUILDS_READ("applications.builds.read", false),
    APPLICATIONS_STORE_UPDATE("applications.store.update", false),
    APPLICATIONS_ENTITLEMENTS("applications.entitlements", false),
    RELATIONSHIPS_READ("relationships.read", true),
    ACTIVITIES_READ("activities.read", true),
    ACTIVITIES_WRITE("activities.write", true);

    private final String scope;
    private final boolean whitelist;

    Scope(String scope, boolean whitelist) {
        this.scope = scope;
        this.whitelist = whitelist;
    }

    public String getScope() {
        return scope;
    }

    public boolean isWhitelist() {
        return whitelist;
    }
}