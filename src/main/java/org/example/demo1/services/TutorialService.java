package org.example.demo1.services;

import org.example.demo1.config.TutorialURLConfig;
import org.example.demo1.models.SubTutorial;
import org.example.demo1.models.Tutorial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to provide tutorial data
 */
public class TutorialService {
    private static TutorialService instance;
    private List<Tutorial> tutorials;
    private Map<String, SubTutorial> subTutorialMap;

    private TutorialService() {
        initializeTutorials();
    }

    public static TutorialService getInstance() {
        if (instance == null) {
            instance = new TutorialService();
        }
        return instance;
    }

    private void initializeTutorials() {
        tutorials = new ArrayList<>();
        subTutorialMap = new HashMap<>();

        // Create the six main tutorials
        Tutorial arraysTutorial = new Tutorial("arrays", "Arrays", "Learn about array data structures");
        Tutorial linkedListTutorial = new Tutorial("linkedlist", "Linked Lists", "Learn about linked list data structures");
        Tutorial stacksTutorial = new Tutorial("stacks", "Stacks", "Learn about stack data structures");
        Tutorial queuesTutorial = new Tutorial("queues", "Queues", "Learn about queue data structures");
        Tutorial treesTutorial = new Tutorial("trees", "Trees", "Learn about tree data structures");
        Tutorial graphsTutorial = new Tutorial("graphs", "Graphs", "Learn about graph data structures");

        // Add sub-tutorials for Arrays
        addSubTutorial(arraysTutorial, "arrays_intro", "Introduction to Arrays", "Basic concepts of arrays");
        addSubTutorial(arraysTutorial, "arrays_insert", "Inserting Elements", "How to insert elements into arrays");
        addSubTutorial(arraysTutorial, "arrays_delete", "Deleting Elements", "How to delete elements from arrays");
        addSubTutorial(arraysTutorial, "arrays_traverse", "Traversing Arrays", "How to traverse through arrays");
        addSubTutorial(arraysTutorial, "arrays_search", "Searching in Arrays", "Linear and binary search in arrays");
        addSubTutorial(arraysTutorial, "arrays_sort", "Sorting Arrays", "Common sorting algorithms for arrays");

        // Add sub-tutorials for Linked Lists
        addSubTutorial(linkedListTutorial, "linkedlist_intro", "Introduction to Linked Lists", "Basic concepts of linked lists");
        addSubTutorial(linkedListTutorial, "linkedlist_types", "Types of Linked Lists", "Singly, doubly, and circular linked lists");
        addSubTutorial(linkedListTutorial, "linkedlist_insert", "Inserting Nodes", "How to insert nodes in linked lists");
        addSubTutorial(linkedListTutorial, "linkedlist_delete", "Deleting Nodes", "How to delete nodes from linked lists");
        addSubTutorial(linkedListTutorial, "linkedlist_traverse", "Traversing Linked Lists", "How to traverse linked lists");
        addSubTutorial(linkedListTutorial, "linkedlist_reverse", "Reversing Linked Lists", "How to reverse a linked list");

        // Add sub-tutorials for Stacks
        addSubTutorial(stacksTutorial, "stacks_intro", "Introduction to Stacks", "Basic concepts of stacks");
        addSubTutorial(stacksTutorial, "stacks_operations", "Stack Operations", "Push, pop, peek, and isEmpty operations");
        addSubTutorial(stacksTutorial, "stacks_implementation", "Implementing Stacks", "Array and linked list implementations");
        addSubTutorial(stacksTutorial, "stacks_applications", "Stack Applications", "Real-world applications of stacks");

        // Add sub-tutorials for Queues
        addSubTutorial(queuesTutorial, "queues_intro", "Introduction to Queues", "Basic concepts of queues");
        addSubTutorial(queuesTutorial, "queues_operations", "Queue Operations", "Enqueue, dequeue, peek, and isEmpty operations");
        addSubTutorial(queuesTutorial, "queues_implementation", "Implementing Queues", "Array and linked list implementations");
        addSubTutorial(queuesTutorial, "queues_types", "Types of Queues", "Simple, circular, priority, and deque");
        addSubTutorial(queuesTutorial, "queues_applications", "Queue Applications", "Real-world applications of queues");

        // Add sub-tutorials for Trees
        addSubTutorial(treesTutorial, "trees_intro", "Introduction to Trees", "Basic concepts of tree data structures");
        addSubTutorial(treesTutorial, "trees_binary", "Binary Trees", "Understanding binary trees");
        addSubTutorial(treesTutorial, "trees_bst", "Binary Search Trees", "Understanding BST properties and operations");
        addSubTutorial(treesTutorial, "trees_avl", "AVL Trees", "Self-balancing binary search trees");
        addSubTutorial(treesTutorial, "trees_traversal", "Tree Traversal", "Pre-order, in-order, post-order, and level-order traversal");
        addSubTutorial(treesTutorial, "trees_applications", "Tree Applications", "Real-world applications of trees");

        // Add sub-tutorials for Graphs
        addSubTutorial(graphsTutorial, "graphs_intro", "Introduction to Graphs", "Basic concepts of graph data structures");
        addSubTutorial(graphsTutorial, "graphs_representation", "Graph Representation", "Adjacency matrix and adjacency list");
        addSubTutorial(graphsTutorial, "graphs_traversal", "Graph Traversal", "BFS and DFS algorithms");
        addSubTutorial(graphsTutorial, "graphs_mst", "Minimum Spanning Tree", "Prim's and Kruskal's algorithms");
        addSubTutorial(graphsTutorial, "graphs_shortest_path", "Shortest Path Algorithms", "Dijkstra's and Bellman-Ford algorithms");
        addSubTutorial(graphsTutorial, "graphs_applications", "Graph Applications", "Real-world applications of graphs");

        // Add tutorials to the list
        tutorials.add(arraysTutorial);
        tutorials.add(linkedListTutorial);
        tutorials.add(stacksTutorial);
        tutorials.add(queuesTutorial);
        tutorials.add(treesTutorial);
        tutorials.add(graphsTutorial);

        // Debug - print all URLs
        for (Tutorial tutorial : tutorials) {
            for (SubTutorial subTutorial : tutorial.getSubTutorials()) {
                System.out.println(subTutorial.getId() + ": " + subTutorial.getUrl());
            }
        }
    }

    private void addSubTutorial(Tutorial tutorial, String id, String name, String description) {
        // Get URL from config
        String url = TutorialURLConfig.getURL(id);
        System.out.println("Loading URL for " + id + ": " + url);

        SubTutorial subTutorial = new SubTutorial(id, name, description, url);
        tutorial.addSubTutorial(subTutorial);
        subTutorialMap.put(id, subTutorial);
    }

    public List<Tutorial> getAllTutorials() {
        return tutorials;
    }

    public Tutorial getTutorialById(String id) {
        for (Tutorial tutorial : tutorials) {
            if (tutorial.getId().equals(id)) {
                return tutorial;
            }
        }
        return null;
    }

    public SubTutorial getSubTutorialById(String id) {
        return subTutorialMap.get(id);
    }
}