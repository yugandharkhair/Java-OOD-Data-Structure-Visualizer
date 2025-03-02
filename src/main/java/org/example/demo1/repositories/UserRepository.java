package org.example.demo1.repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.example.demo1.models.User;
import org.example.demo1.utils.MongoDBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserRepository {
    private static final String COLLECTION_NAME = "users";

    // Get the users collection
    private MongoCollection<Document> getUsersCollection() {
        return MongoDBConnection.getDatabase().getCollection(COLLECTION_NAME);
    }

    /**
     * Create a new user in the database
     * @param user The user to create
     * @return The created user with ID
     */
    public User createUser(User user) {
        // Hash the password before storing
        user.setPasswordHash(BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt()));

        // Insert the user document
        InsertOneResult result = getUsersCollection().insertOne(user.toDocument());

        // Set the generated ID to the user object
        if (result.getInsertedId() != null) {
            user.setId(result.getInsertedId().asObjectId().getValue());
        }

        return user;
    }

    /**
     * Find a user by username
     * @param username The username to search for
     * @return The user object if found, null otherwise
     */
    public User findByUsername(String username) {
        Document doc = getUsersCollection().find(Filters.eq("username", username)).first();
        return doc != null ? User.fromDocument(doc) : null;
    }

    /**
     * Find a user by email
     * @param email The email to search for
     * @return The user object if found, null otherwise
     */
    public User findByEmail(String email) {
        Document doc = getUsersCollection().find(Filters.eq("email", email)).first();
        return doc != null ? User.fromDocument(doc) : null;
    }

    /**
     * Find a user by ID
     * @param id The user ID
     * @return The user object if found, null otherwise
     */
    public User findById(ObjectId id) {
        Document doc = getUsersCollection().find(Filters.eq("_id", id)).first();
        return doc != null ? User.fromDocument(doc) : null;
    }

    /**
     * Update a user's information
     * @param user The user to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateUser(User user) {
        Bson filter = Filters.eq("_id", user.getId());
        Document updateDoc = user.toDocument();
        // Remove _id field from update document
        updateDoc.remove("_id");

        UpdateResult result = getUsersCollection().replaceOne(filter, updateDoc);
        return result.getModifiedCount() > 0;
    }

    /**
     * Update a user's last login time
     * @param userId The user ID
     * @return true if update was successful, false otherwise
     */
    public boolean updateLastLogin(ObjectId userId) {
        Bson filter = Filters.eq("_id", userId);
        Bson update = Updates.set("lastLogin", new Date());

        UpdateResult result = getUsersCollection().updateOne(filter, update);
        return result.getModifiedCount() > 0;
    }

    /**
     * Authenticate a user
     * @param username The username
     * @param password The plaintext password
     * @return The authenticated user if successful, null otherwise
     */
    public User authenticate(String username, String password) {
        User user = findByUsername(username);

        if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
            // Update last login time
            updateLastLogin(user.getId());
            return user;
        }

        return null;
    }

    /**
     * Check if a username is already taken
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return getUsersCollection().countDocuments(Filters.eq("username", username)) > 0;
    }

    /**
     * Check if an email is already registered
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return getUsersCollection().countDocuments(Filters.eq("email", email)) > 0;
    }

    /**
     * Get all users
     * @return List of all users
     */
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        FindIterable<Document> documents = getUsersCollection().find();

        for (Document doc : documents) {
            users.add(User.fromDocument(doc));
        }

        return users;
    }
}