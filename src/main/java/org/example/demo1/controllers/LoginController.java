package org.example.demo1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo1.models.User;
import org.example.demo1.repositories.UserRepository;
import org.example.demo1.utils.UserSession;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginController extends BaseController {

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Label loginMessage;

    @FXML
    private TextField registerUsername;

    @FXML
    private TextField registerEmail;

    @FXML
    private PasswordField registerPassword;

    @FXML
    private PasswordField registerConfirmPassword;

    @FXML
    private Label registerMessage;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = loginUsername.getText().trim();
        String password = loginPassword.getText();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            loginMessage.setText("Please enter both username and password");
            return;
        }

        // Attempt to authenticate
        User user = userRepository.authenticate(username, password);

        if (user != null) {
            // Authentication successful
            UserSession.getInstance().startSession(user);

            // Navigate to dashboard or profile
            navigateToDashboard();
        } else {
            // Authentication failed
            loginMessage.setText("Invalid username or password");
        }
    }

    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        String username = registerUsername.getText().trim();
        String email = registerEmail.getText().trim();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();

        // Reset error message
        registerMessage.setText("");

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registerMessage.setText("Please fill in all fields");
            return;
        }

        // Validate username (alphanumeric and at least 3 characters)
        if (!username.matches("^[a-zA-Z0-9]{3,}$")) {
            registerMessage.setText("Username must be at least 3 alphanumeric characters");
            return;
        }

        // Validate email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            registerMessage.setText("Please enter a valid email address");
            return;
        }

        // Check password length
        if (password.length() < 6) {
            registerMessage.setText("Password must be at least 6 characters");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            registerMessage.setText("Passwords do not match");
            return;
        }

        // Check if username already exists
        if (userRepository.usernameExists(username)) {
            registerMessage.setText("Username already taken");
            return;
        }

        // Check if email already exists
        if (userRepository.emailExists(email)) {
            registerMessage.setText("Email already registered");
            return;
        }

        // Create new user
        User newUser = new User(username, email, password);
        try {
            userRepository.createUser(newUser);

            // Automatically login the new user
            UserSession.getInstance().startSession(newUser);

            // Show success message and navigate
            registerMessage.setText("Registration successful!");
            registerMessage.setStyle("-fx-text-fill: green;");

            // Navigate to dashboard after brief delay
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::navigateToDashboard);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            registerMessage.setText("Error registering user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo1/dashboard.fxml"));
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
            System.err.println("Error loading dashboard.fxml");
        }
    }
}