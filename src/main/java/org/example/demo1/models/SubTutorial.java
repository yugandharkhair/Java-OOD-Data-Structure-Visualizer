package org.example.demo1.models;

import java.util.ArrayList;
import java.util.List;

/**
        * Represents a sub-tutorial (e.g., Inserting Element, Deleting, etc.)
 */
public class SubTutorial {
    private String id;
    private String name;
    private String description;
    private String url;

    public SubTutorial(String id, String name, String description, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}