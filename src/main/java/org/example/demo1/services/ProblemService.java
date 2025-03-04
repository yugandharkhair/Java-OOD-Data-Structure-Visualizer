package org.example.demo1.services;

import org.example.demo1.models.Problem;
import org.example.demo1.models.ProblemCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to provide problem data
 */
public class ProblemService {
    private static ProblemService instance;
    private List<ProblemCategory> categories;
    private Map<String, Problem> problemMap;

    private ProblemService() {
        initializeProblems();
    }

    public static ProblemService getInstance() {
        if (instance == null) {
            instance = new ProblemService();
        }
        return instance;
    }

    private void initializeProblems() {
        categories = new ArrayList<>();
        problemMap = new HashMap<>();

        // Create the main problem categories
        ProblemCategory arraysCategory = new ProblemCategory("arrays", "Arrays", "Practice array problems");
        ProblemCategory linkedListCategory = new ProblemCategory("linked_lists", "Linked Lists", "Practice linked list problems");
        ProblemCategory stacksCategory = new ProblemCategory("stacks", "Stacks", "Practice stack problems");
        ProblemCategory queuesCategory = new ProblemCategory("queues", "Queues", "Practice queue problems");
        ProblemCategory treesCategory = new ProblemCategory("trees", "Trees", "Practice tree problems");
        ProblemCategory graphsCategory = new ProblemCategory("graphs", "Graphs", "Practice graph problems");

        // Add problems for Arrays
        addProblem(arraysCategory, "arrays_binary_search", "Binary Search", "Implement binary search algorithm in a sorted array.", "Easy", "https://leetcode.com/problems/binary-search/");
        addProblem(arraysCategory, "arrays_two_sum", "Two Sum", "Find two numbers in an array that add up to a target.", "Easy", "https://leetcode.com/problems/two-sum/");
        addProblem(arraysCategory, "arrays_merge_intervals", "Merge Intervals", "Merge overlapping intervals in an array.", "Medium", "https://leetcode.com/problems/merge-intervals/");
        addProblem(arraysCategory, "arrays_median_sorted", "Median of Two Sorted Arrays", "Find the median of two sorted arrays.", "Hard", "https://leetcode.com/problems/median-of-two-sorted-arrays/");

        // Add problems for Linked Lists
        addProblem(linkedListCategory, "ll_reverse", "Reverse Linked List", "Reverse a singly linked list.", "Easy", "https://leetcode.com/problems/reverse-linked-list/");
        addProblem(linkedListCategory, "ll_merge_two", "Merge Two Sorted Lists", "Merge two sorted linked lists.", "Easy", "https://leetcode.com/problems/merge-two-sorted-lists/");
        addProblem(linkedListCategory, "ll_cycle", "Linked List Cycle", "Detect if a linked list has a cycle.", "Medium", "https://leetcode.com/problems/linked-list-cycle/");
        addProblem(linkedListCategory, "ll_lru_cache", "LRU Cache", "Implement an LRU cache using a linked list.", "Hard", "https://leetcode.com/problems/lru-cache/");

        // Add problems for Stacks
        addProblem(stacksCategory, "stacks_valid_parentheses", "Valid Parentheses", "Check if a string of parentheses is valid.", "Easy", "https://leetcode.com/problems/valid-parentheses/");
        addProblem(stacksCategory, "stacks_min_stack", "Min Stack", "Design a stack that supports push, pop, top, and retrieving the minimum element.", "Medium", "https://leetcode.com/problems/min-stack/");
        addProblem(stacksCategory, "stacks_largest_rect", "Largest Rectangle in Histogram", "Find the largest rectangle in a histogram.", "Hard", "https://leetcode.com/problems/largest-rectangle-in-histogram/");

        // Add problems for Queues
        addProblem(queuesCategory, "queues_implement", "Implement Queue using Stacks", "Implement a queue using stacks.", "Easy", "https://leetcode.com/problems/implement-queue-using-stacks/");
        addProblem(queuesCategory, "queues_sliding_window", "Sliding Window Maximum", "Find the maximum element in each sliding window.", "Hard", "https://leetcode.com/problems/sliding-window-maximum/");

        // Add problems for Trees
        addProblem(treesCategory, "trees_max_depth", "Maximum Depth of Binary Tree", "Find the maximum depth of a binary tree.", "Easy", "https://leetcode.com/problems/maximum-depth-of-binary-tree/");
        addProblem(treesCategory, "trees_level_order", "Binary Tree Level Order Traversal", "Traverse a binary tree in level order.", "Medium", "https://leetcode.com/problems/binary-tree-level-order-traversal/");
        addProblem(treesCategory, "trees_validate_bst", "Validate Binary Search Tree", "Check if a binary tree is a valid BST.", "Medium", "https://leetcode.com/problems/validate-binary-search-tree/");

        // Add problems for Graphs
        addProblem(graphsCategory, "graphs_bfs", "BFS Traversal", "Perform breadth-first search on a graph.", "Medium", "https://leetcode.com/problems/course-schedule/");
        addProblem(graphsCategory, "graphs_dfs", "DFS Traversal", "Perform depth-first search on a graph.", "Medium", "https://leetcode.com/problems/number-of-islands/");
        addProblem(graphsCategory, "graphs_shortest_path", "Shortest Path", "Find the shortest path in a graph.", "Hard", "https://leetcode.com/problems/network-delay-time/");

        // Add categories to the list
        categories.add(arraysCategory);
        categories.add(linkedListCategory);
        categories.add(stacksCategory);
        categories.add(queuesCategory);
        categories.add(treesCategory);
        categories.add(graphsCategory);
    }

    private void addProblem(ProblemCategory category, String id, String title, String description, String difficulty, String url) {
        Problem problem = new Problem(id, title, description, difficulty, url);
        category.addProblem(problem);
        problemMap.put(id, problem);
    }

    public List<ProblemCategory> getAllCategories() {
        return categories;
    }

    public ProblemCategory getCategoryById(String id) {
        for (ProblemCategory category : categories) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }

    public Problem getProblemById(String id) {
        return problemMap.get(id);
    }
}