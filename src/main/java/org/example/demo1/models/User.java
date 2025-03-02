package org.example.demo1.models;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private ObjectId id;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private Date registrationDate;
    private Date lastLogin;
    private List<String> completedTutorials;
    private List<String> completedProblems;

    // Constructors
    public User() {
        this.registrationDate = new Date();
        this.completedTutorials = new ArrayList<>();
        this.completedProblems = new ArrayList<>();
    }

    public User(String username, String email, String passwordHash) {
        this();
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Factory method to create User from MongoDB Document
    public static User fromDocument(Document doc) {
        User user = new User();
        user.id = doc.getObjectId("_id");
        user.username = doc.getString("username");
        user.email = doc.getString("email");
        user.passwordHash = doc.getString("passwordHash");
        user.fullName = doc.getString("fullName");
        user.registrationDate = doc.getDate("registrationDate");
        user.lastLogin = doc.getDate("lastLogin");

        // Handle arrays
        List<String> tutorials = doc.getList("completedTutorials", String.class);
        user.completedTutorials = tutorials != null ? tutorials : new ArrayList<>();

        List<String> problems = doc.getList("completedProblems", String.class);
        user.completedProblems = problems != null ? problems : new ArrayList<>();

        return user;
    }

    // Convert User to MongoDB Document
    public Document toDocument() {
        Document doc = new Document();

        if (id != null) {
            doc.append("_id", id);
        }

        doc.append("username", username)
                .append("email", email)
                .append("passwordHash", passwordHash)
                .append("registrationDate", registrationDate)
                .append("completedTutorials", completedTutorials)
                .append("completedProblems", completedProblems);

        if (fullName != null) {
            doc.append("fullName", fullName);
        }

        if (lastLogin != null) {
            doc.append("lastLogin", lastLogin);
        }

        return doc;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<String> getCompletedTutorials() {
        return completedTutorials;
    }

    public void setCompletedTutorials(List<String> completedTutorials) {
        this.completedTutorials = completedTutorials;
    }

    public List<String> getCompletedProblems() {
        return completedProblems;
    }

    public void setCompletedProblems(List<String> completedProblems) {
        this.completedProblems = completedProblems;
    }

    public void addCompletedTutorial(String tutorialId) {
        if (!completedTutorials.contains(tutorialId)) {
            completedTutorials.add(tutorialId);
        }
    }

    public void addCompletedProblem(String problemId) {
        if (!completedProblems.contains(problemId)) {
            completedProblems.add(problemId);
        }
    }
}