package org.example.demo1.controllers.problems;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import java.awt.Desktop;
import java.net.URI;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemsController {

    @FXML
    private Button backButton;

    @FXML
    private Label titleLabel;

    @FXML
    private TableView<Problem> problemsTable;

    @FXML
    private TableColumn<Problem, String> titleColumn;

    @FXML
    private TableColumn<Problem, String> difficultyColumn;

    @FXML
    private TableColumn<Problem, String> linkColumn;

    private static String problemType;

    private static final Map<String, List<Problem>> problemsMap = new HashMap<>();

    static {
        problemsMap.put("arrays", List.of(
                new Problem("Binary Search", "Easy", "https://leetcode.com/problems/merge-sorted-array/"),
                new Problem("Two Sum", "Easy", "https://leetcode.com/problems/remove-element/"),
                new Problem("Merge Intervals", "Medium", "https://leetcode.com/problems/remove-duplicates-from-sorted-array/"),
                new Problem("Median of Two Sorted Arrays", "Hard", "https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/")
        ));

        problemsMap.put("linked_lists", List.of(
                new Problem("Reverse Linked List", "Easy", "https://leetcode.com/problems/reverse-linked-list/"),
                new Problem("Merge Two Sorted Lists", "Easy", "https://leetcode.com/problems/merge-two-sorted-lists/"),
                new Problem("Linked List Cycle", "Medium", "https://leetcode.com/problems/linked-list-cycle/"),
                new Problem("LRU Cache", "Hard", "https://leetcode.com/problems/lru-cache/")
        ));

        problemsMap.put("stacks", List.of(
                new Problem("Valid Parentheses", "Easy", "https://leetcode.com/problems/valid-parentheses/"),
                new Problem("Min Stack", "Medium", "https://leetcode.com/problems/min-stack/"),
                new Problem("Largest Rectangle in Histogram", "Hard", "https://leetcode.com/problems/largest-rectangle-in-histogram/")
        ));

        problemsMap.put("queues", List.of(
                new Problem("Implement Queue using Stacks", "Easy", "https://leetcode.com/problems/implement-queue-using-stacks/"),
                new Problem("Sliding Window Maximum", "Hard", "https://leetcode.com/problems/sliding-window-maximum/")
        ));
    }

    public static void setProblemType(String type) {
        problemType = type;
    }

    @FXML
    public void initialize() {
        if (problemType == null) {
            problemType = "arrays"; // Default if nothing is set
        }

        titleLabel.setText(problemType.substring(0, 1).toUpperCase() + problemType.substring(1) + " Problems");

        // Set up column bindings
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        linkColumn.setCellValueFactory(new PropertyValueFactory<>("link"));

        // Custom Cell Factory for Hyperlink Column
        linkColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Problem, String> call(TableColumn<Problem, String> param) {
                return new TableCell<>() {
                    private final Hyperlink link = new Hyperlink("🔗");

                    @Override
                    protected void updateItem(String url, boolean empty) {
                        super.updateItem(url, empty);
                        if (empty || url == null) {
                            setGraphic(null);
                        } else {
                            link.setOnAction(e -> openURL(url));
                            setGraphic(link);
                        }
                    }
                };
            }
        });

        // Populate Table with Problems
        problemsTable.setItems(FXCollections.observableArrayList(problemsMap.getOrDefault(problemType, List.of())));
    }

    private void openURL(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) {
        navigateToScreen("/org/example/demo1/problems_frontend/problemsdashboard.fxml");
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

    public static class Problem {
        private final String title;
        private final String difficulty;
        private final String link;

        public Problem(String title, String difficulty, String link) {
            this.title = title;
            this.difficulty = difficulty;
            this.link = link;
        }

        public String getTitle() { return title; }
        public String getDifficulty() { return difficulty; }
        public String getLink() { return link; }
    }
}
