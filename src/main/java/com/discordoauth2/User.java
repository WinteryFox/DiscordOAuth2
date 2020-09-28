package com.discordoauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String id;
    private String username;
    private String avatar;
    private String discriminator;
    private int flags;
    @JsonProperty("public_flags")
    private int publicFlags;
    private String locale;
    @JsonProperty("mfa_enabled")
    private boolean isMfaEnabled;
    @JsonProperty("premium_type")
    private short premium;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getPublicFlags() {
        return publicFlags;
    }

    public void setPublicFlags(int publicFlags) {
        this.publicFlags = publicFlags;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isMfaEnabled() {
        return isMfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        isMfaEnabled = mfaEnabled;
    }

    public short getPremium() {
        return premium;
    }

    public void setPremium(short premium) {
        this.premium = premium;
    }
}