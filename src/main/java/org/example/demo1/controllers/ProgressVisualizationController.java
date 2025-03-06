package org.example.demo1.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.demo1.models.Problem;
import org.example.demo1.models.ProblemCategory;
import org.example.demo1.models.Tutorial;
import org.example.demo1.models.User;
import org.example.demo1.services.ProblemService;
import org.example.demo1.services.TutorialService;
import org.example.demo1.utils.UserSession;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ProgressVisualizationController extends BaseController implements Initializable {

    @FXML
    private Label progressSummaryLabel;
    @FXML
    private PieChart tutorialPieChart;
    @FXML
    private PieChart problemsPieChart;
    @FXML
    private Label tutorialProgressLabel;
    @FXML
    private Label problemsProgressLabel;
    @FXML
    private BarChart<String, Number> tutorialCategoriesChart;
    @FXML
    private BarChart<String, Number> problemCategoriesChart;
    @FXML
    private StackedBarChart<String, Number> problemDifficultyChart;
    @FXML
    private VBox tutorialStatsContainer;
    @FXML
    private VBox problemStatsContainer;

    private TutorialService tutorialService;
    private ProblemService problemService;
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tutorialService = TutorialService.getInstance();
        problemService = ProblemService.getInstance();
        currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser == null) {
            // Redirect to login if not logged in
            navigateToLogin();
            return;
        }

        // Initialize the charts and statistics
        setupTutorialProgress();
        setupProblemProgress();
        setupDifficultyDistribution();

        // Additional styling for the chart legend items
        for (PieChart chart : new PieChart[] { tutorialPieChart, problemsPieChart }) {
            chart.setLegendVisible(true);
            chart.setLabelsVisible(true);
        }
    }

    /**
     * Apply colors to a pie chart and ensure that both the slices and legend match
     * @param chart The pie chart to style
     * @param completedColor Color for the "Completed" slice
     * @param remainingColor Color for the "Remaining" slice
     */
    private void applyColorsToPieChart(PieChart chart, String completedColor, String remainingColor) {
        // Set initial colors
        PieChart.Data completedSlice = chart.getData().get(0); // "Completed" slice
        PieChart.Data remainingSlice = chart.getData().get(1); // "Remaining" slice

        // We need to use a listener because the nodes are created after the data is set
        chart.lookupAll(".default-color0.chart-pie").forEach(node ->
                node.setStyle("-fx-pie-color: " + completedColor + ";"));
        chart.lookupAll(".default-color1.chart-pie").forEach(node ->
                node.setStyle("-fx-pie-color: " + remainingColor + ";"));

        // Add listener to apply colors when the chart is shown
        chart.idProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) {
                // Apply to pie slices
                completedSlice.getNode().setStyle("-fx-pie-color: " + completedColor + ";");
                remainingSlice.getNode().setStyle("-fx-pie-color: " + remainingColor + ";");

                // Apply to legend
                for (Node node : chart.lookupAll(".chart-legend-item-symbol")) {
                    if (node.getStyleClass().contains("default-color0")) {
                        node.setStyle("-fx-background-color: " + completedColor + ";");
                    } else if (node.getStyleClass().contains("default-color1")) {
                        node.setStyle("-fx-background-color: " + remainingColor + ";");
                    }
                }
            }
        });
    }

    private void setupTutorialProgress() {
        List<Tutorial> allTutorials = tutorialService.getAllTutorials();
        int totalSubTutorials = 0;
        int completedSubTutorials = 0;

        // Calculate overall statistics
        for (Tutorial tutorial : allTutorials) {
            totalSubTutorials += tutorial.getSubTutorials().size();
        }
        completedSubTutorials = currentUser.getCompletedTutorials().size();

        int tutorialPercentage = totalSubTutorials > 0 ?
                (int)((double)completedSubTutorials / totalSubTutorials * 100) : 0;

        // Update the labels
        tutorialProgressLabel.setText(tutorialPercentage + "% Complete (" +
                completedSubTutorials + " of " + totalSubTutorials + " topics)");

        // Setup the pie chart
        ObservableList<PieChart.Data> tutorialPieData = FXCollections.observableArrayList(
                new PieChart.Data("Completed", completedSubTutorials),
                new PieChart.Data("Remaining", totalSubTutorials - completedSubTutorials)
        );
        tutorialPieChart.setData(tutorialPieData);
        tutorialPieChart.setTitle("Tutorial Completion");

        // Apply styling to chart slices when they are created
        final String tutorialBlue = "#304ffe";
        final String gray = "#e0e0e0";

        applyColorsToPieChart(tutorialPieChart, tutorialBlue, gray);

        // Setup the tutorial categories bar chart
        XYChart.Series<String, Number> tutorialSeries = new XYChart.Series<>();
        tutorialSeries.setName("Completion Percentage");

        // Add category statistics to the bar chart and container
        tutorialStatsContainer.getChildren().clear();

        for (Tutorial tutorial : allTutorials) {
            double progress = tutorial.getCompletionPercentage(currentUser.getCompletedTutorials());
            tutorialSeries.getData().add(new XYChart.Data<>(tutorial.getName(), progress));

            // Add to the stats container
            HBox tutorialBox = createProgressBox(
                    tutorial.getName(),
                    progress,
                    tutorial.getSubTutorials().size(),
                    tutorialBlue);
            tutorialStatsContainer.getChildren().add(tutorialBox);
        }

        tutorialCategoriesChart.getData().add(tutorialSeries);
    }

    private void setupProblemProgress() {
        List<ProblemCategory> categories = problemService.getAllCategories();
        int totalProblems = 0;
        int completedProblems = 0;

        // Count total problems and completed ones
        for (ProblemCategory category : categories) {
            totalProblems += category.getProblems().size();
        }
        completedProblems = currentUser.getCompletedProblems().size();

        int problemPercentage = totalProblems > 0 ?
                (int)((double)completedProblems / totalProblems * 100) : 0;

        // Update the labels
        problemsProgressLabel.setText(problemPercentage + "% Complete (" +
                completedProblems + " of " + totalProblems + " problems)");

        // Setup the pie chart
        ObservableList<PieChart.Data> problemPieData = FXCollections.observableArrayList(
                new PieChart.Data("Completed", completedProblems),
                new PieChart.Data("Remaining", totalProblems - completedProblems)
        );
        problemsPieChart.setData(problemPieData);
        problemsPieChart.setTitle("Problem Completion");

        // Apply styling to chart slices
        final String problemGreen = "#43a047";
        final String gray = "#e0e0e0";

        applyColorsToPieChart(problemsPieChart, problemGreen, gray);

        // Setup the problem categories bar chart
        XYChart.Series<String, Number> problemSeries = new XYChart.Series<>();
        problemSeries.setName("Completion Percentage");

        // Add category statistics to the bar chart and container
        problemStatsContainer.getChildren().clear();

        for (ProblemCategory category : categories) {
            double progress = category.getCompletionPercentage(currentUser.getCompletedProblems());
            problemSeries.getData().add(new XYChart.Data<>(category.getName(), progress));

            // Add to the stats container
            HBox problemBox = createProgressBox(
                    category.getName(),
                    progress,
                    category.getProblems().size(),
                    problemGreen);
            problemStatsContainer.getChildren().add(problemBox);
        }

        problemCategoriesChart.getData().add(problemSeries);
    }

    private void setupDifficultyDistribution() {
        // Count problems by difficulty
        Map<String, Integer> totalByDifficulty = new HashMap<>();
        Map<String, Integer> completedByDifficulty = new HashMap<>();

        // Initialize difficulty levels
        totalByDifficulty.put("Easy", 0);
        totalByDifficulty.put("Medium", 0);
        totalByDifficulty.put("Hard", 0);
        completedByDifficulty.put("Easy", 0);
        completedByDifficulty.put("Medium", 0);
        completedByDifficulty.put("Hard", 0);

        // Count problems by difficulty
        for (ProblemCategory category : problemService.getAllCategories()) {
            for (Problem problem : category.getProblems()) {
                String difficulty = problem.getDifficulty();

                // Increment total count
                totalByDifficulty.put(difficulty, totalByDifficulty.getOrDefault(difficulty, 0) + 1);

                // Check if completed
                if (currentUser.getCompletedProblems().contains(problem.getId())) {
                    completedByDifficulty.put(difficulty, completedByDifficulty.getOrDefault(difficulty, 0) + 1);
                }
            }
        }

        // Create series for the stacked bar chart
        XYChart.Series<String, Number> completedSeries = new XYChart.Series<>();
        completedSeries.setName("Completed");

        XYChart.Series<String, Number> remainingSeries = new XYChart.Series<>();
        remainingSeries.setName("Remaining");

        // Add data to the series
        for (String difficulty : new String[]{"Easy", "Medium", "Hard"}) {
            int total = totalByDifficulty.getOrDefault(difficulty, 0);
            int completed = completedByDifficulty.getOrDefault(difficulty, 0);
            int remaining = total - completed;

            completedSeries.getData().add(new XYChart.Data<>(difficulty, completed));
            remainingSeries.getData().add(new XYChart.Data<>(difficulty, remaining));
        }

        // Add the series to the chart
        problemDifficultyChart.getData().addAll(completedSeries, remainingSeries);
    }

    private HBox createProgressBox(String name, double percentage, int total, String colorHex) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: white; -fx-border-color: #f0f0f0; -fx-border-radius: 4;");

        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(150);
        nameLabel.setStyle("-fx-font-weight: bold;");

        ProgressBar progressBar = new ProgressBar(percentage / 100.0);
        progressBar.setPrefWidth(200);
        progressBar.setStyle("-fx-accent: " + colorHex + ";");

        // Count completed items
        int completed = (int) Math.round(percentage * total / 100.0);

        Label statsLabel = new Label(String.format("%.0f%% (%d/%d)", percentage, completed, total));

        box.getChildren().addAll(nameLabel, progressBar, statsLabel);
        return box;
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        navigateToDashboard();
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