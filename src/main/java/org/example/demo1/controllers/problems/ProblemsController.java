package org.example.demo1.controllers.problems;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo1.controllers.BaseController;
import org.example.demo1.models.Problem;
import org.example.demo1.models.ProblemCategory;
import org.example.demo1.models.User;
import org.example.demo1.repositories.UserRepository;
import org.example.demo1.services.ProblemService;
import org.example.demo1.utils.UserSession;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ProblemsController extends BaseController implements Initializable {

    @FXML
    private ScrollPane problemsScrollPane;

    @FXML
    private VBox problemsContainer;

    private final ProblemService problemService = ProblemService.getInstance();
    private final UserRepository userRepository = new UserRepository();
    private User currentUser;

    // Maps to keep track of UI elements
    private Map<String, TitledPane> categoryPaneMap = new HashMap<>();
    private Map<String, ProgressBar> progressBarMap = new HashMap<>();
    private Map<String, Label> progressLabelMap = new HashMap<>();
    private Map<String, Label> completionLabelMap = new HashMap<>();

    // Static field to store the currently selected category
    private static String selectedCategoryId;

    public static void setSelectedCategory(String categoryId) {
        selectedCategoryId = categoryId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get current user
        currentUser = UserSession.getInstance().getCurrentUser();

        // Load problems into UI
        loadProblems();
    }

    private void loadProblems() {
        problemsContainer.getChildren().clear();
        problemsContainer.setSpacing(20);
        problemsContainer.setPadding(new Insets(20));

        // Clear maps
        categoryPaneMap.clear();
        progressBarMap.clear();
        progressLabelMap.clear();
        completionLabelMap.clear();

        // If a specific category is selected, only show that one
        if (selectedCategoryId != null) {
            ProblemCategory category = problemService.getCategoryById(selectedCategoryId);
            if (category != null) {
                createCategorySection(category);

                // Auto-expand this category since it's specifically selected
                TitledPane pane = categoryPaneMap.get(category.getId());
                if (pane != null) {
                    pane.setExpanded(true);
                }
            }
        } else {
            // Otherwise show all categories
            for (ProblemCategory category : problemService.getAllCategories()) {
                createCategorySection(category);
            }
        }
    }

    private void createCategorySection(ProblemCategory category) {
        // Create a section for this problem category
        TitledPane categoryPane = new TitledPane();

        // Create a header with title and completion badge
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Category title
        Label titleLabel = new Label(category.getName());
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Completion badge/label
        Label completionLabel = new Label("");
        completionLabel.setVisible(false); // Hide initially
        completionLabel.setPadding(new Insets(2, 8, 2, 8));
        completionLabel.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                "-fx-background-radius: 4; -fx-font-size: 12px;");
        completionLabelMap.put(category.getId(), completionLabel);

        headerBox.getChildren().addAll(titleLabel, completionLabel);
        categoryPane.setGraphic(headerBox);

        categoryPane.setCollapsible(true);
        categoryPane.setExpanded(false);

        // Store reference to pane
        categoryPaneMap.put(category.getId(), categoryPane);

        // Create content for the category
        VBox categoryContent = new VBox(10);
        categoryContent.setPadding(new Insets(10));

        // Add description
        Label descriptionLabel = new Label(category.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14px;");
        categoryContent.getChildren().add(descriptionLabel);

        // Add progress indicator
        double progress = 0.0;
        if (currentUser != null) {
            progress = category.getCompletionPercentage(currentUser.getCompletedProblems());
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
        progressBarMap.put(category.getId(), progressBar);

        Label progressLabel = new Label(String.format("%.0f%% Complete", progress));
        progressLabelMap.put(category.getId(), progressLabel);

        progressBox.getChildren().addAll(progressBar, progressLabel);
        categoryContent.getChildren().add(progressBox);

        // Add separator
        categoryContent.getChildren().add(new Separator());

        // Add problems
        for (Problem problem : category.getProblems()) {
            createProblemItem(problem, categoryContent, category);
        }

        categoryPane.setContent(categoryContent);

        // Check if all problems are completed and update visuals
        updateCategoryCompletionVisuals(category);

        problemsContainer.getChildren().add(categoryPane);
    }

    private void updateCategoryCompletionVisuals(ProblemCategory category) {
        TitledPane pane = categoryPaneMap.get(category.getId());
        ProgressBar progressBar = progressBarMap.get(category.getId());
        Label completionLabel = completionLabelMap.get(category.getId());

        if (pane == null || progressBar == null || completionLabel == null) return;

        // Check if user is logged in
        if (currentUser == null) {
            // Reset styles for non-logged in users
            pane.setStyle("");
            completionLabel.setVisible(false);
            return;
        }

        // Calculate completion
        double progress = category.getCompletionPercentage(currentUser.getCompletedProblems());
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

    private void createProblemItem(Problem problem, VBox container, ProblemCategory parentCategory) {
        HBox problemBox = new HBox(10);
        problemBox.setAlignment(Pos.CENTER_LEFT);

        // Create checkbox to mark completion
        CheckBox completedCheckBox = new CheckBox();

        // Check if this problem is completed by the user
        boolean isCompleted = currentUser != null &&
                currentUser.getCompletedProblems().contains(problem.getId());

        completedCheckBox.setSelected(isCompleted);

        // Style completed items
        if (isCompleted) {
            problemBox.setStyle("-fx-background-color: #F1F8E9;"); // Very light green
        }

        // Create hyperlink to open the problem in browser
        Hyperlink problemLink = new Hyperlink(problem.getTitle());
        if (isCompleted) {
            problemLink.setStyle("-fx-text-fill: #388E3C;"); // Darker green for completed items
        }

        problemLink.setOnAction(event -> openProblemLink(problem));

        // Add completion listener
        completedCheckBox.setOnAction(event -> {
            if (currentUser != null) {
                if (completedCheckBox.isSelected()) {
                    currentUser.addCompletedProblem(problem.getId());
                    problemBox.setStyle("-fx-background-color: #F1F8E9;"); // Add background when checked

                    // Update hyperlink color
                    problemLink.setStyle("-fx-text-fill: #388E3C;"); // Darker green for completed items
                } else {
                    currentUser.getCompletedProblems().remove(problem.getId());
                    problemBox.setStyle(""); // Remove background when unchecked

                    // Reset hyperlink color
                    problemLink.setStyle(""); // Default color
                }

                // Update user in database
                boolean updateSuccess = userRepository.updateUser(currentUser);

                if (updateSuccess) {
                    // Update progress display without rebuilding entire UI
                    updateProgress(parentCategory);
                    System.out.println("User progress updated successfully for problem: " + problem.getId());
                } else {
                    System.err.println("Failed to update user progress for problem: " + problem.getId());
                }
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



        // Add difficulty label
        Label difficultyLabel = new Label(problem.getDifficulty());
        difficultyLabel.setPadding(new Insets(2, 8, 2, 8));

        // Set color based on difficulty
        String difficultyStyle = "-fx-background-radius: 4; -fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: ";
        switch (problem.getDifficulty().toLowerCase()) {
            case "easy":
                difficultyStyle += "#4CAF50;"; // Green
                break;
            case "medium":
                difficultyStyle += "#FF9800;"; // Orange
                break;
            case "hard":
                difficultyStyle += "#F44336;"; // Red
                break;
            default:
                difficultyStyle += "#757575;"; // Gray
        }
        difficultyLabel.setStyle(difficultyStyle);

        // Add tooltip with description
        Tooltip tooltip = new Tooltip(problem.getDescription());
        Tooltip.install(problemLink, tooltip);

        problemBox.getChildren().addAll(completedCheckBox, problemLink, difficultyLabel);

        // Add padding for better appearance
        problemBox.setPadding(new Insets(5, 0, 5, 0));

        container.getChildren().add(problemBox);
    }

    private void updateProgress(ProblemCategory category) {
        // Get progress bar and label for this category
        ProgressBar progressBar = progressBarMap.get(category.getId());
        Label progressLabel = progressLabelMap.get(category.getId());

        if (progressBar == null || progressLabel == null) return;

        // Calculate and update progress
        double progress = category.getCompletionPercentage(currentUser.getCompletedProblems());
        progressBar.setProgress(progress / 100.0);
        progressLabel.setText(String.format("%.0f%% Complete", progress));

        // Update styling based on completion
        updateCategoryCompletionVisuals(category);
    }

    private void openProblemLink(Problem problem) {
        // Debug: Print the URL to console
        System.out.println("Attempting to open URL: " + problem.getUrl());

        // Check if URL is specified
        if (problem.getUrl() == null || problem.getUrl().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problem Link");
            alert.setHeaderText(null);
            alert.setContentText("Problem link not available yet. Please check back later.");
            alert.showAndWait();
            return;
        }

        // Try to open the URL using Desktop class
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(problem.getUrl()));
                return; // Success, return early
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("Failed to open URL with Desktop: " + e.getMessage());
        }

        // Show the URL to the user as a fallback
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Problem Link");
        alert.setHeaderText("Please copy and paste this URL in your browser:");
        alert.setContentText(problem.getUrl());
        alert.showAndWait();
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        // If viewing a specific category, clear the selection and go back to problems dashboard
        ProblemsController.setSelectedCategory(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo1/problems_frontend/problemsdashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading problemsdashboard.fxml");
        }
    }

    private void navigateToScreen(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxmlFile);
        }
    }
}