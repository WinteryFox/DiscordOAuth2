package com.discordoauth2;

public enum Scope {
    BOT("bot"),
    CONNECTIONS("connections"),
    EMAIL("email"),
    IDENTIFY("identify"),
    GUILDS("guilds"),
    GUILDS_JOIN("guilds.join"),
    GDM_JOIN("gdm.join"),
    MESSAGES_READ("messages.read"),
    RPC("rpc"),
    RPC_API("rpc.api"),
    RPC_NOTIFICATIONS_READ("rpc.notifications.read"),
    WEBHOOK_INCOMING("webhook.incoming"),
    APPLICATIONS_BUILDS_UPLOAD("applications.builds.upload"),
    APPLICATIONS_BUILDS_READ("applications.builds.read"),
    APPLICATIONS_STORE_UPDATE("applications.store.update"),
    APPLICATIONS_ENTITLEMENTS("applications.entitlement"),
    RELATIONSHIPS_READ("relationships.read"),
    ACTIVITIES_READ("activities.read"),
    ACTIVITIES_WRITE("activities.write");

    private final String scope;

    Scope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }
}