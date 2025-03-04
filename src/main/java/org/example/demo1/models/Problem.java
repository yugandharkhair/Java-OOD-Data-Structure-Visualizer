package org.example.demo1.models;

/**
 * Represents an individual problem (e.g., Two Sum, Binary Search, etc.)
 */
public class Problem {
    private String id;
    private String title;
    private String description;
    private String difficulty;
    private String url;

    public Problem(String id, String title, String description, String difficulty, String url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.url = url;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getUrl() {
        return url;
    }
}