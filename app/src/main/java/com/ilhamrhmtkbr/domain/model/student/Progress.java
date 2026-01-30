package com.ilhamrhmtkbr.domain.model.student;

public class Progress {
    private final String id;
    private final String title;

    private final String completedSections;
    private final String totalSections;

    public Progress(String id, String title, String completedSections, String totalSections) {
        this.id = id;
        this.title = title;
        this.completedSections = completedSections;
        this.totalSections = totalSections;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompletedSections() {
        return completedSections;
    }

    public String getTotalSections() {
        return totalSections;
    }
}