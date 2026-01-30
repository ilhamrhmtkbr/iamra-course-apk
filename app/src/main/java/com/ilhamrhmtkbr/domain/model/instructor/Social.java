package com.ilhamrhmtkbr.domain.model.instructor;

public class Social {
    private final String id;
    private final String urLink;
    private final String appName;
    private final String displayName;

    public Social(String id, String urLink, String appName, String displayName) {
        this.id = id;
        this.urLink = urLink;
        this.appName = appName;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getUrLink() {
        return urLink;
    }

    public String getAppName() {
        return appName;
    }

    public String getDisplayName() {
        return displayName;
    }
}