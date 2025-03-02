package org.example.demo1.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for tutorial URLs.
 * Pre-filled with GeeksForGeeks and other reputable resources.
 */
public class TutorialURLConfig {
    private static final Map<String, String> tutorialURLs = new HashMap<>();

    static {
        // Arrays tutorials
        tutorialURLs.put("arrays_intro", "https://www.geeksforgeeks.org/introduction-to-arrays/");
        tutorialURLs.put("arrays_insert", "https://www.geeksforgeeks.org/array-data-structure/#insertion");
        tutorialURLs.put("arrays_delete", "https://www.geeksforgeeks.org/array-data-structure/#deletion");
        tutorialURLs.put("arrays_traverse", "https://www.geeksforgeeks.org/array-data-structure/#traversal");
        tutorialURLs.put("arrays_search", "https://www.geeksforgeeks.org/linear-search/");
        tutorialURLs.put("arrays_sort", "https://www.geeksforgeeks.org/sorting-algorithms/");

        // Linked List tutorials
        tutorialURLs.put("linkedlist_intro", "https://www.geeksforgeeks.org/data-structures/linked-list/");
        tutorialURLs.put("linkedlist_types", "https://www.geeksforgeeks.org/types-of-linked-list/");
        tutorialURLs.put("linkedlist_insert", "https://www.geeksforgeeks.org/linked-list-set-2-inserting-a-node/");
        tutorialURLs.put("linkedlist_delete", "https://www.geeksforgeeks.org/linked-list-set-3-deleting-node/");
        tutorialURLs.put("linkedlist_traverse", "https://www.geeksforgeeks.org/linked-list-set-1-introduction/");
        tutorialURLs.put("linkedlist_reverse", "https://www.geeksforgeeks.org/reverse-a-linked-list/");

        // Stack tutorials
        tutorialURLs.put("stacks_intro", "https://www.geeksforgeeks.org/stack-data-structure/");
        tutorialURLs.put("stacks_operations", "https://www.geeksforgeeks.org/stack-data-structure-introduction-program/");
        tutorialURLs.put("stacks_implementation", "https://www.geeksforgeeks.org/stack-data-structure-introduction-program/");
        tutorialURLs.put("stacks_applications", "https://www.geeksforgeeks.org/applications-of-stack-data-structure/");

        // Queue tutorials
        tutorialURLs.put("queues_intro", "https://www.geeksforgeeks.org/queue-data-structure/");
        tutorialURLs.put("queues_operations", "https://www.geeksforgeeks.org/queue-set-1introduction/");
        tutorialURLs.put("queues_implementation", "https://www.geeksforgeeks.org/queue-linked-list-implementation/");
        tutorialURLs.put("queues_types", "https://www.geeksforgeeks.org/types-of-queues-in-data-structure/");
        tutorialURLs.put("queues_applications", "https://www.geeksforgeeks.org/applications-of-queue-data-structure/");

        // Tree tutorials
        tutorialURLs.put("trees_intro", "https://www.geeksforgeeks.org/introduction-to-tree-data-structure/");
        tutorialURLs.put("trees_binary", "https://www.geeksforgeeks.org/binary-tree-data-structure/");
        tutorialURLs.put("trees_bst", "https://www.geeksforgeeks.org/binary-search-tree-data-structure/");
        tutorialURLs.put("trees_avl", "https://www.geeksforgeeks.org/avl-tree-set-1-insertion/");
        tutorialURLs.put("trees_traversal", "https://www.geeksforgeeks.org/tree-traversals-inorder-preorder-and-postorder/");
        tutorialURLs.put("trees_applications", "https://www.geeksforgeeks.org/applications-of-tree-data-structure/");

        // Graph tutorials
        tutorialURLs.put("graphs_intro", "https://www.geeksforgeeks.org/graph-data-structure-and-algorithms/");
        tutorialURLs.put("graphs_representation", "https://www.geeksforgeeks.org/graph-and-its-representations/");
        tutorialURLs.put("graphs_traversal", "https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/");
        tutorialURLs.put("graphs_mst", "https://www.geeksforgeeks.org/prims-minimum-spanning-tree-mst-greedy-algo-5/");
        tutorialURLs.put("graphs_shortest_path", "https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/");
        tutorialURLs.put("graphs_applications", "https://www.geeksforgeeks.org/applications-of-graph-data-structure/");

    }

    /**
     * Get the URL for a specific tutorial ID
     * @param tutorialId the ID of the tutorial
     * @return the URL for the tutorial, or null if not found
     */
    public static String getURL(String tutorialId) {
        return tutorialURLs.get(tutorialId);
    }
}