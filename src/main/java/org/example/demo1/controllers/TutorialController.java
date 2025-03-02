package org.example.demo1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.example.demo1.models.SubTutorial;
import org.example.demo1.models.Tutorial;
import org.example.demo1.models.User;
import org.example.demo1.repositories.UserRepository;
import org.example.demo1.services.TutorialService;
import org.example.demo1.utils.UserSession;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TutorialController extends BaseController implements Initializable {

    @FXML
    private ScrollPane tutorialScrollPane;

    @FXML
    private VBox tutorialsContainer;

    private final TutorialService tutorialService = TutorialService.getInstance();
    private final UserRepository userRepository = new UserRepository();
    private User currentUser;

    // Maps to keep track of UI elements
    private Map<String, TitledPane> tutorialPaneMap = new HashMap<>();
    private Map<String, ProgressBar> progressBarMap = new HashMap<>();
    private Map<String, Label> progressLabelMap = new HashMap<>();
    private Map<String, Label> completionLabelMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get current user
        currentUser = UserSession.getInstance().getCurrentUser();

        // Load tutorials into UI
        loadTutorials();
    }

    private void loadTutorials() {
        tutorialsContainer.getChildren().clear();
        tutorialsContainer.setSpacing(20);
        tutorialsContainer.setPadding(new Insets(20));

        // Clear maps
        tutorialPaneMap.clear();
        progressBarMap.clear();
        progressLabelMap.clear();
        completionLabelMap.clear();

        // Get all tutorials
        for (Tutorial tutorial : tutorialService.getAllTutorials()) {
            createTutorialSection(tutorial);
        }
    }

    private void createTutorialSection(Tutorial tutorial) {
        // Create a section for this tutorial category
        TitledPane tutorialPane = new TitledPane();

        // Create a header with title and completion badge
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Tutorial title
        Label titleLabel = new Label(tutorial.getName());
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Completion badge/label
        Label completionLabel = new Label("");
        completionLabel.setVisible(false); // Hide initially
        completionLabel.setPadding(new Insets(2, 8, 2, 8));
        completionLabel.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                "-fx-background-radius: 4; -fx-font-size: 12px;");
        completionLabelMap.put(tutorial.getId(), completionLabel);

        headerBox.getChildren().addAll(titleLabel, completionLabel);
        tutorialPane.setGraphic(headerBox);

        tutorialPane.setCollapsible(true);
        tutorialPane.setExpanded(false);

        // Store reference to pane
        tutorialPaneMap.put(tutorial.getId(), tutorialPane);

        // Create content for the tutorial
        VBox tutorialContent = new VBox(10);
        tutorialContent.setPadding(new Insets(10));

        // Add description
        Label descriptionLabel = new Label(tutorial.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14px;");
        tutorialContent.getChildren().add(descriptionLabel);

        // Add progress indicator
        double progress = 0.0;
        if (currentUser != null) {
            progress = tutorial.getCompletionPercentage(currentUser.getCompletedTutorials());
        }

        HBox progressBox = new HBox(10);
        progressBox.setAlignment(Pos.CENTER_LEFT);

        ProgressBar progressBar = new ProgressBar(progress / 100.0);
        progressBar.setPrefWidth(200);
        // Set progress bar color based on completion percentage
        if (progress >= 100) {
            progressBar.setStyle("-fx-accent: #4CAF50;"); // Green for complete
        } else if (progress > 0) {
            progressBar.setStyle("-fx-accent: #2196F3;"); // Blue for in progress
        }
        progressBarMap.put(tutorial.getId(), progressBar);

        Label progressLabel = new Label(String.format("%.0f%% Complete", progress));
        progressLabelMap.put(tutorial.getId(), progressLabel);

        progressBox.getChildren().addAll(progressBar, progressLabel);
        tutorialContent.getChildren().add(progressBox);

        // Add separator
        tutorialContent.getChildren().add(new Separator());

        // Add sub-tutorials
        for (SubTutorial subTutorial : tutorial.getSubTutorials()) {
            createSubTutorialItem(subTutorial, tutorialContent, tutorial);
        }

        tutorialPane.setContent(tutorialContent);

        // Check if all sub-tutorials are completed and update visuals
        updateTutorialCompletionVisuals(tutorial);

        tutorialsContainer.getChildren().add(tutorialPane);
    }

    private void updateTutorialCompletionVisuals(Tutorial tutorial) {
        TitledPane pane = tutorialPaneMap.get(tutorial.getId());
        ProgressBar progressBar = progressBarMap.get(tutorial.getId());
        Label completionLabel = completionLabelMap.get(tutorial.getId());

        if (pane == null || progressBar == null || completionLabel == null) return;

        // Check if user is logged in
        if (currentUser == null) {
            // Reset styles for non-logged in users
            pane.setStyle("");
            completionLabel.setVisible(false);
            return;
        }

        // Calculate completion
        double progress = tutorial.getCompletionPercentage(currentUser.getCompletedTutorials());
        boolean isComplete = progress >= 100.0;

        // Update ProgressBar color
        if (isComplete) {
            progressBar.setStyle("-fx-accent: #4CAF50;"); // Green for complete
        } else if (progress > 0) {
            progressBar.setStyle("-fx-accent: #2196F3;"); // Blue for in progress
        } else {
            progressBar.setStyle(""); // Default for not started
        }

        // Update Completion Label
        if (isComplete) {
            completionLabel.setText("COMPLETED");
            completionLabel.setVisible(true);

            // Add a subtle green border and background to the pane
            pane.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 1px; " +
                    "-fx-background-color: #F1F8E9;"); // Light green background
        } else {
            completionLabel.setVisible(false);
            pane.setStyle(""); // Reset style
        }
    }

    private void createSubTutorialItem(SubTutorial subTutorial, VBox container, Tutorial parentTutorial) {
        HBox subTutorialBox = new HBox(10);
        subTutorialBox.setAlignment(Pos.CENTER_LEFT);

        // Create checkbox to mark completion
        CheckBox completedCheckBox = new CheckBox();

        // Check if this tutorial is completed by the user
        boolean isCompleted = currentUser != null &&
                currentUser.getCompletedTutorials().contains(subTutorial.getId());

        completedCheckBox.setSelected(isCompleted);

        // Style completed items
        if (isCompleted) {
            subTutorialBox.setStyle("-fx-background-color: #F1F8E9;"); // Very light green
        }

        // Add completion listener
        completedCheckBox.setOnAction(event -> {
            if (currentUser != null) {
                if (completedCheckBox.isSelected()) {
                    currentUser.addCompletedTutorial(subTutorial.getId());
                    subTutorialBox.setStyle("-fx-background-color: #F1F8E9;"); // Add background when checked
                } else {
                    currentUser.getCompletedTutorials().remove(subTutorial.getId());
                    subTutorialBox.setStyle(""); // Remove background when unchecked
                }

                // Update user in database
                userRepository.updateUser(currentUser);

                // Update progress display without rebuilding entire UI
                updateProgress(parentTutorial);
            } else {
                // If not logged in, show alert and uncheck
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Required");
                alert.setHeaderText(null);
                alert.setContentText("Please log in to track your progress.");
                alert.showAndWait();

                completedCheckBox.setSelected(false);
            }
        });

        // Create hyperlink to open the tutorial in browser
        Hyperlink tutorialLink = new Hyperlink(subTutorial.getName());
        if (isCompleted) {
            tutorialLink.setStyle("-fx-text-fill: #388E3C;"); // Darker green for completed items
        }

        tutorialLink.setOnAction(event -> openTutorialLink(subTutorial));

        // Add tooltip with description
        Tooltip tooltip = new Tooltip(subTutorial.getDescription());
        Tooltip.install(tutorialLink, tooltip);

        subTutorialBox.getChildren().addAll(completedCheckBox, tutorialLink);

        // Add padding for better appearance
        subTutorialBox.setPadding(new Insets(5, 0, 5, 0));

        container.getChildren().add(subTutorialBox);
    }

    private void updateProgress(Tutorial tutorial) {
        // Get progress bar and label for this tutorial
        ProgressBar progressBar = progressBarMap.get(tutorial.getId());
        Label progressLabel = progressLabelMap.get(tutorial.getId());

        if (progressBar == null || progressLabel == null) return;

        // Calculate and update progress
        double progress = tutorial.getCompletionPercentage(currentUser.getCompletedTutorials());
        progressBar.setProgress(progress / 100.0);
        progressLabel.setText(String.format("%.0f%% Complete", progress));

        // Update styling based on completion
        updateTutorialCompletionVisuals(tutorial);
    }

    private void openTutorialLink(SubTutorial subTutorial) {
        // Debug: Print the URL to console
        System.out.println("Attempting to open URL: " + subTutorial.getUrl());

        // Check if URL is specified
        if (subTutorial.getUrl() == null || subTutorial.getUrl().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tutorial Link");
            alert.setHeaderText(null);
            alert.setContentText("Tutorial link not available yet. Please check back later.");
            alert.showAndWait();
            return;
        }

        // Try multiple methods to open the URL

        // Method 1: Using Desktop class (may not work in all environments)
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(subTutorial.getUrl()));
                return; // Success, return early
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("Failed to open URL with Desktop: " + e.getMessage());
            // Continue to fallback methods
        }

        // Method 2: Using WebView in a new window
        try {
            Stage webViewStage = new Stage();
            webViewStage.setTitle(subTutorial.getName());

            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load(subTutorial.getUrl());

            VBox root = new VBox();
            root.getChildren().add(webView);
            VBox.setVgrow(webView, Priority.ALWAYS);

            Scene scene = new Scene(root, 1024, 768);
            webViewStage.setScene(scene);
            webViewStage.show();
            return; // Success, return early
        } catch (Exception e) {
            System.err.println("Failed to open URL with WebView: " + e.getMessage());
            // Continue to fallback methods
        }

        // Method 3: Show the URL to the user as a last resort
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tutorial Link");
        alert.setHeaderText("Please copy and paste this URL in your browser:");
        alert.setContentText(subTutorial.getUrl());
        alert.showAndWait();
    }
}