package org.example.demo1.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a problem category (e.g., Arrays, LinkedList, Stacks, etc.)
 */
public class ProblemCategory {
    private String id;
    private String name;
    private String description;
    private List<Problem> problems;

    public ProblemCategory(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.problems = new ArrayList<>();
    }

    // Add a problem to this category
    public void addProblem(Problem problem) {
        this.problems.add(problem);
    }

    // Calculate the completion percentage based on completed problems
    public double getCompletionPercentage(List<String> completedProblemIds) {
        if (problems.isEmpty()) {
            return 0.0;
        }

        int completedCount = 0;
        for (Problem problem : problems) {
            if (completedProblemIds.contains(problem.getId())) {
                completedCount++;
            }
        }

        return (double) completedCount / problems.size() * 100.0;
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

    public List<Problem> getProblems() {
        return problems;
    }
}