package org.example.demo1.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a main tutorial category (e.g., Arrays, LinkedList)
 */
public class Tutorial {
    private String id;
    private String name;
    private String description;
    private List<SubTutorial> subTutorials;

    public Tutorial(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subTutorials = new ArrayList<>();
    }

    // Add a sub-tutorial to this tutorial
    public void addSubTutorial(SubTutorial subTutorial) {
        this.subTutorials.add(subTutorial);
    }

    // Calculate the completion percentage based on completed sub-tutorials
    public double getCompletionPercentage(List<String> completedTutorialIds) {
        if (subTutorials.isEmpty()) {
            return 0.0;
        }

        int completedCount = 0;
        for (SubTutorial subTutorial : subTutorials) {
            if (completedTutorialIds.contains(subTutorial.getId())) {
                completedCount++;
            }
        }

        return (double) completedCount / subTutorials.size() * 100.0;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<SubTutorial> getSubTutorials() {
        return subTutorials;
    }
}
