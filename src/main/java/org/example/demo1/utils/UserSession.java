package org.example.demo1.utils;

import org.example.demo1.models.User;

/**
 * Singleton class to manage user session throughout the application
 */
public class UserSession {
    private static UserSession instance;
    private User currentUser;

    private UserSession() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Get the singleton instance
     * @return The UserSession instance
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Start a new session with the given user
     * @param user The authenticated user
     */
    public void startSession(User user) {
        this.currentUser = user;
    }

    /**
     * End the current session
     */
    public void endSession() {
        this.currentUser = null;
    }

    /**
     * Get the current user
     * @return The current user, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if a user is logged in
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}