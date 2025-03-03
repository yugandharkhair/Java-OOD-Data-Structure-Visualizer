package org.example.demo1.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo1.controllers.problems.ProblemsController;
import org.example.demo1.models.Problem;
import org.example.demo1.models.ProblemCategory;
import org.example.demo1.models.Tutorial;
import org.example.demo1.models.User;
import org.example.demo1.services.ProblemService;
import org.example.demo1.services.TutorialService;
import org.example.demo1.utils.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private Button catalogButton;

    @FXML
    private Button visualizationButton;

    @FXML
    private Button tutorialButton;

    @FXML
    private Button problemsButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button viewProgressButton;

    @FXML
    private Button continueButton;

    @FXML
    private Button practiceButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label tutorialCompletionLabel;

    @FXML
    private Label problemCompletionLabel;

    @FXML
    private Label recommendedTutorialLabel;

    @FXML
    private Label recommendedProblemLabel;

    @FXML
    private ProgressBar recommendedTutorialProgress;

    @FXML
    private VBox statsPanel;

    @FXML
    private VBox recommendedPanel;

    private User currentUser;
    private TutorialService tutorialService;
    private ProblemService problemService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tutorialService = TutorialService.getInstance();
        problemService = ProblemService.getInstance();

        // Check if user is logged in
        currentUser = UserSession.getInstance().getCurrentUser();
        boolean isLoggedIn = (currentUser != null);

        if (isLoggedIn) {
            // Update login button to show username
            loginButton.setText("My Account");

            // Update welcome message with username
            welcomeLabel.setText("Welcome back, " + currentUser.getUsername() + "!");

            // Enable all buttons
            enableAllButtons(true);

            // Show stats panel and recommended panel
            statsPanel.setVisible(true);
            statsPanel.setManaged(true);
            recommendedPanel.setVisible(true);
            recommendedPanel.setManaged(true);

            // Update statistics
            updateStatistics();

            // Update recommended content
            updateRecommendedContent();
        } else {
            // Reset login button
            loginButton.setText("Login");

            // Reset welcome message
            welcomeLabel.setText("Welcome to Data Structures Visualizer");

            // Disable all buttons except login
            enableAllButtons(false);

            // Hide stats panel and recommended panel - this is crucial!
            statsPanel.setVisible(false);
            statsPanel.setManaged(false);
            recommendedPanel.setVisible(false);
            recommendedPanel.setManaged(false);
        }
    }

    private void updateStatistics() {
        if (currentUser == null) return;

        // Calculate tutorial completion percentage
        List<Tutorial> allTutorials = tutorialService.getAllTutorials();
        int totalSubTutorials = 0;
        int completedSubTutorials = 0;

        for (Tutorial tutorial : allTutorials) {
            totalSubTutorials += tutorial.getSubTutorials().size();
        }

        completedSubTutorials = currentUser.getCompletedTutorials().size();

        int tutorialPercentage = totalSubTutorials > 0 ?
                (int)((double)completedSubTutorials / totalSubTutorials * 100) : 0;

        tutorialCompletionLabel.setText(tutorialPercentage + "%");

        // Calculate problem completion percentage
        int completedProblems = currentUser.getCompletedProblems() != null ?
                currentUser.getCompletedProblems().size() : 0;

        // Count total problems from all categories
        int totalProblems = 0;
        for (ProblemCategory category : problemService.getAllCategories()) {
            totalProblems += category.getProblems().size();
        }

        int problemPercentage = totalProblems > 0 ?
                (int)((double)completedProblems / totalProblems * 100) : 0;

        problemCompletionLabel.setText(problemPercentage + "%");
    }

    private void updateRecommendedContent() {
        if (currentUser == null) return;

        // Find a tutorial that's not completed to recommend
        Tutorial recommendedTutorial = null;
        for (Tutorial tutorial : tutorialService.getAllTutorials()) {
            double progress = tutorial.getCompletionPercentage(currentUser.getCompletedTutorials());
            if (progress > 0 && progress < 100) {
                recommendedTutorial = tutorial;
                break;
            }
        }

        // If no in-progress tutorial found, suggest the first uncompleted one
        if (recommendedTutorial == null) {
            for (Tutorial tutorial : tutorialService.getAllTutorials()) {
                if (tutorial.getCompletionPercentage(currentUser.getCompletedTutorials()) < 100) {
                    recommendedTutorial = tutorial;
                    break;
                }
            }
        }

        // Update the UI with the recommended tutorial
        if (recommendedTutorial != null) {
            recommendedTutorialLabel.setText(recommendedTutorial.getName() + ": Continue Learning");
            double progress = recommendedTutorial.getCompletionPercentage(currentUser.getCompletedTutorials()) / 100.0;
            recommendedTutorialProgress.setProgress(progress);
        } else {
            recommendedTutorialLabel.setText("All tutorials completed!");
            recommendedTutorialProgress.setProgress(1.0);
        }

        // Find a problem to recommend
        Problem recommendedProblem = findRecommendedProblem();
        if (recommendedProblem != null) {
            // Set a clear recommended problem text with difficulty
            recommendedProblemLabel.setText(recommendedProblem.getTitle() + " (" + recommendedProblem.getDifficulty() + ")");

            // Change the practice button to include the category name
            String categoryName = "";
            for (ProblemCategory category : problemService.getAllCategories()) {
                if (category.getProblems().contains(recommendedProblem)) {
                    categoryName = category.getName();
                    break;
                }
            }

            if (!categoryName.isEmpty()) {
                practiceButton.setText("Practice " + categoryName);
            }

            // Store the category ID for navigation
            practiceButton.setUserData(getContainingCategoryId(recommendedProblem));
        } else {
            recommendedProblemLabel.setText("All problems completed!");
            practiceButton.setText("Practice Problems");
        }
    }

    private Problem findRecommendedProblem() {
        if (currentUser == null) return null;

        // Strategy 1: First, look for an "Easy" problem that hasn't been completed
        for (ProblemCategory category : problemService.getAllCategories()) {
            for (Problem problem : category.getProblems()) {
                if (!currentUser.getCompletedProblems().contains(problem.getId()) &&
                        problem.getDifficulty().equalsIgnoreCase("Easy")) {
                    return problem;
                }
            }
        }

        // Strategy 2: Then look for any "Medium" problem
        for (ProblemCategory category : problemService.getAllCategories()) {
            for (Problem problem : category.getProblems()) {
                if (!currentUser.getCompletedProblems().contains(problem.getId()) &&
                        problem.getDifficulty().equalsIgnoreCase("Medium")) {
                    return problem;
                }
            }
        }

        // Strategy 3: Finally, any uncompleted problem
        for (ProblemCategory category : problemService.getAllCategories()) {
            for (Problem problem : category.getProblems()) {
                if (!currentUser.getCompletedProblems().contains(problem.getId())) {
                    return problem;
                }
            }
        }

        return null; // All problems completed
    }

    private String getContainingCategoryId(Problem problem) {
        if (problem == null) return null;

        for (ProblemCategory category : problemService.getAllCategories()) {
            for (Problem p : category.getProblems()) {
                if (p.getId().equals(problem.getId())) {
                    return category.getId();
                }
            }
        }

        return null;
    }

    private void enableAllButtons(boolean enable) {
        // Only enable these buttons if the user is logged in
        catalogButton.setDisable(!enable);
        visualizationButton.setDisable(!enable);
        tutorialButton.setDisable(!enable);
        problemsButton.setDisable(!enable);
        profileButton.setDisable(!enable);

        // Set opacity to show disabled state more clearly
        double opacity = enable ? 1.0 : 0.7;

        catalogButton.setOpacity(opacity);
        visualizationButton.setOpacity(opacity);
        tutorialButton.setOpacity(opacity);
        problemsButton.setOpacity(opacity);
        profileButton.setOpacity(opacity);
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        if (currentUser != null) {
            // If already logged in, go to profile
            navigateToScreen("org/example/demo1/profile.fxml");
        } else {
            // Otherwise go to login
            navigateToScreen("org/example/demo1/login.fxml");
        }
    }

    @FXML
    private void onCatalogButtonClick(ActionEvent event) {
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/catalog.fxml");
        } else {
            promptLogin();
        }
    }

    @FXML
    private void onVisualizationButtonClick(ActionEvent event) {
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/visualization.fxml");
        } else {
            promptLogin();
        }
    }

    @FXML
    private void onTutorialButtonClick(ActionEvent event) {
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/tutorial.fxml");
        } else {
            promptLogin();
        }
    }

    @FXML
    private void onProblemsButtonClick(ActionEvent event) {
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/problems_frontend/problemsdashboard.fxml");
        } else {
            promptLogin();
        }
    }

    @FXML
    private void onProfileButtonClick(ActionEvent event) {
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/profile.fxml");
        } else {
            promptLogin();
        }
    }

    @FXML
    private void onContinueLearningClick(ActionEvent event) {
        // Go to tutorials page
        if (UserSession.getInstance().isLoggedIn()) {
            navigateToScreen("org/example/demo1/tutorial.fxml");
        }
    }

    @FXML
    private void onPracticeButtonClick(ActionEvent event) {
        // Check if logged in
        if (!UserSession.getInstance().isLoggedIn()) {
            promptLogin();
            return;
        }

        // Get the selected category from userData if available
        String categoryId = (String) practiceButton.getUserData();

        if (categoryId != null) {
            // Set the selected category in ProblemsController
            ProblemsController.setSelectedCategory(categoryId);

            // Navigate to the problems page directly
            navigateToScreen("org/example/demo1/problems_frontend/problems.fxml");
        } else {
            // Navigate to problems dashboard if no specific category
            navigateToScreen("org/example/demo1/problems_frontend/problemsdashboard.fxml");
        }
    }

    private void promptLogin() {
        // Show login dialog
        navigateToScreen("org/example/demo1/login.fxml");
    }

    private void navigateToScreen(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxmlFile);
        }
    }
}