package org.example.demo1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo1.models.User;
import org.example.demo1.repositories.UserRepository;
import org.example.demo1.utils.UserSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class ProfileController extends BaseController implements Initializable {

    @FXML
    private Label usernameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label joinDateLabel;

    @FXML
    private TextField fullNameField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label updateMessage;

    @FXML
    private Label passwordMessage;

    @FXML
    private Button logoutButton;

    @FXML
    private Button viewProgressButton;

    private final UserRepository userRepository = new UserRepository();
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get the current user from session
        currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser == null) {
            // No user logged in, redirect to login
            navigateToLogin();
            return;
        }

        // Populate UI with user data
        usernameLabel.setText(currentUser.getUsername());
        emailLabel.setText(currentUser.getEmail());

        // Format and display join date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        joinDateLabel.setText("Member since: " + dateFormat.format(currentUser.getRegistrationDate()));

        // Set full name if available
        if (currentUser.getFullName() != null && !currentUser.getFullName().isEmpty()) {
            fullNameField.setText(currentUser.getFullName());
        }
    }

    @FXML
    protected void onUpdateProfileClick(ActionEvent event) {
        String fullName = fullNameField.getText().trim();

        // Update user data
        currentUser.setFullName(fullName);

        // Save to database
        boolean success = userRepository.updateUser(currentUser);

        if (success) {
            updateMessage.setText("Profile updated successfully!");
            updateMessage.setStyle("-fx-text-fill: green;");
        } else {
            updateMessage.setText("Failed to update profile");
            updateMessage.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    protected void onChangePasswordClick(ActionEvent event) {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Reset message
        passwordMessage.setText("");

        // Validate input
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            passwordMessage.setText("Please fill in all password fields");
            passwordMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        // Check if current password is correct
        if (!BCrypt.checkpw(currentPassword, currentUser.getPasswordHash())) {
            passwordMessage.setText("Current password is incorrect");
            passwordMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        // Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            passwordMessage.setText("New passwords do not match");
            passwordMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        // Check password length
        if (newPassword.length() < 6) {
            passwordMessage.setText("New password must be at least 6 characters");
            passwordMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        // Hash the new password
        String newPasswordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        currentUser.setPasswordHash(newPasswordHash);

        // Update in database
        boolean success = userRepository.updateUser(currentUser);

        if (success) {
            passwordMessage.setText("Password changed successfully!");
            passwordMessage.setStyle("-fx-text-fill: green;");

            // Clear password fields
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
        } else {
            passwordMessage.setText("Failed to change password");
            passwordMessage.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    protected void onLogoutButtonClick(ActionEvent event) {
        // End the user session
        UserSession.getInstance().endSession();

        // Navigate to dashboard
        navigateToDashboard();
    }

    @FXML
    protected void onViewProgressButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo1/progress-visualization.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) viewProgressButton.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();

            Scene newScene = new Scene(root,width, height);
            stage.setScene(newScene);

            // Ensure no animation effect (maximize only if not already maximized)
            if (!stage.isMaximized()) {
                stage.setMaximized(true);
            }
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading progress-visualization.fxml");
        }
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo1/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();

            Scene newScene = new Scene(root,width, height);
            stage.setScene(newScene);

            // Ensure no animation effect (maximize only if not already maximized)
            if (!stage.isMaximized()) {
                stage.setMaximized(true);
            }
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading login.fxml");
        }
    }
}