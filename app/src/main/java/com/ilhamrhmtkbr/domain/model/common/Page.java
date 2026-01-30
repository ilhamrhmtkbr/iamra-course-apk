package com.ilhamrhmtkbr.domain.model.common;

public class Page {
    private final String url;
    private final String label;
    private final Boolean active;

    public Page(String url, String label, Boolean active) {
        this.url = url;
        this.label = label;
        this.active = active;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public Boolean getActive() {
        return active;
    }
}
