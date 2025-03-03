package org.example.demo1.controllers.problems;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo1.controllers.BaseController;
import org.example.demo1.models.ProblemCategory;
import org.example.demo1.models.User;
import org.example.demo1.services.ProblemService;
import org.example.demo1.utils.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProblemDashboardController extends BaseController implements Initializable {

    @FXML
    private VBox categoriesContainer;

    private final ProblemService problemService = ProblemService.getInstance();
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get current user
        currentUser = UserSession.getInstance().getCurrentUser();

        // Load problem categories
        loadProblemCategories();
    }

    private void loadProblemCategories() {
        categoriesContainer.getChildren().clear();
        categoriesContainer.setSpacing(20);

        // Add heading
        Label headerLabel = new Label("Choose a Data Structure to Practice");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        categoriesContainer.getChildren().add(headerLabel);

        // Add subheading
        Label subheadingLabel = new Label("Click on a category to see problems and track your progress");
        subheadingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #757575;");
        categoriesContainer.getChildren().add(subheadingLabel);

        // Create a card for each problem category
        for (ProblemCategory category : problemService.getAllCategories()) {
            categoriesContainer.getChildren().add(createCategoryCard(category));
        }
    }

    private VBox createCategoryCard(ProblemCategory category) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);

        // Category title
        Label titleLabel = new Label(category.getName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Category description
        Label descriptionLabel = new Label(category.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #757575;");
        descriptionLabel.setWrapText(true);

        // Problem count
        Label countLabel = new Label(category.getProblems().size() + " problems available");
        countLabel.setStyle("-fx-font-size: 14px;");

        // Progress bar (if user is logged in)
        VBox progressBox = new VBox(5);
        progressBox.setVisible(currentUser != null);
        progressBox.setManaged(currentUser != null);

        if (currentUser != null) {
            double progress = category.getCompletionPercentage(currentUser.getCompletedProblems());

            ProgressBar progressBar = new ProgressBar(progress / 100.0);
            progressBar.setPrefWidth(Double.MAX_VALUE);

            // Set color based on progress
            if (progress >= 100) {
                progressBar.setStyle("-fx-accent: #4CAF50;"); // Green for complete
            } else if (progress > 0) {
                progressBar.setStyle("-fx-accent: #2196F3;"); // Blue for in progress
            }

            Label progressLabel = new Label(String.format("%.0f%% Complete", progress));
            progressLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #757575;");

            progressBox.getChildren().addAll(progressBar, progressLabel);
        }

        // View problems button
        Button viewButton = new Button("View Problems");
        viewButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        viewButton.setPrefWidth(200);

        viewButton.setOnAction(event -> {
            ProblemsController.setSelectedCategory(category.getId());
            navigateToProblems();
        });

        // Add all components to the card
        card.getChildren().addAll(titleLabel, descriptionLabel, countLabel, progressBox, viewButton);

        return card;
    }

    private void navigateToProblems() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo1/problems_frontend/problems.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading problems.fxml");
        }
    }

    @Override
    protected void onBackButtonClick(ActionEvent event) {
        navigateToDashboard();
    }
}