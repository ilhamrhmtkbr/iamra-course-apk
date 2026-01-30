package com.ilhamrhmtkbr.domain.model.forum;

public class Group {
    private  String id;
    private String title;
    private String description;
    private String image;
    private String editor;

    public Group(String id, String title, String description, String image, String editor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.editor = editor;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getEditor() {
        return editor;
    }
}