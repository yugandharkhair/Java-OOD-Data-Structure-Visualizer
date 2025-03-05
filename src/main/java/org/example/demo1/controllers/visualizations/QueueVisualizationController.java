package org.example.demo1.controllers.visualizations;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class QueueVisualizationController {

    private static final int BOX_WIDTH = 80;
    private static final int BOX_HEIGHT = 40;
    private static final int MAX_SIZE = 10; // Max elements in queue visualization

    private final Queue<Integer> queue = new LinkedList<>();

    @FXML
    private HBox queueContainer; // HBox for queue visualization

    @FXML
    private TextField inputField;

    @FXML
    private Button enqueueButton, dequeueButton, peekButton;

    @FXML
    protected Button backButton;

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        navigateToDashboard();
    }

    @FXML
    public void initialize() {
        enqueueButton.setOnAction(e -> enqueueElement());
        dequeueButton.setOnAction(e -> dequeueElement());
        peekButton.setOnAction(e -> peekElement());
    }

    private void enqueueElement() {
        if (queue.size() >= MAX_SIZE) return; // Prevent overflow

        try {
            int value = Integer.parseInt(inputField.getText());
            queue.add(value);

            // Create a new queue item
            StackPane queueItem = new StackPane();
            Rectangle box = new Rectangle(BOX_WIDTH, BOX_HEIGHT, Color.LIGHTBLUE);
            box.setArcWidth(10);
            box.setArcHeight(10);

            Text text = new Text(String.valueOf(value));
            text.setFill(Color.WHITE);
            text.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

            queueItem.getChildren().addAll(box, text);

            // Add new element to the START of the queue (Left side of HBox)
            queueContainer.getChildren().add(0, queueItem);

            // Fade-in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), queueItem);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            inputField.clear();
        } catch (NumberFormatException e) {
            inputField.setText("Invalid");
        }
    }

    private void dequeueElement() {
        if (queue.isEmpty()) return; // Prevent underflow

        queue.poll(); // Remove from queue (FIFO)

        if (!queueContainer.getChildren().isEmpty()) {
            StackPane frontElement = (StackPane) queueContainer.getChildren().get(queueContainer.getChildren().size() - 1); // Get last element

            // Fade-out animation before removing
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), frontElement);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                queueContainer.getChildren().remove(frontElement);
            });
            fadeOut.play();
        }
    }

    private void peekElement() {
        if (queue.isEmpty()) return;

        StackPane frontElement = (StackPane) queueContainer.getChildren().get(queueContainer.getChildren().size() - 1); // Get last element
        Rectangle box = (Rectangle) frontElement.getChildren().get(0);

        // Highlight effect for the front element
        box.setFill(Color.web("#2F8093FF"));

        // Restore to original color after a delay
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                javafx.application.Platform.runLater(() -> box.setFill(Color.LIGHTBLUE));
            } catch (InterruptedException ignored) {
            }
        }).start();
    }


    protected void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo1/visualization.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading dashboard.fxml");
        }
    }
}
