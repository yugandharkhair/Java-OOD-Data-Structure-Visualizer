package org.example.demo1.controllers.visualizations;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Stack;

public class StackVisualizationController {

    private static final int BOX_WIDTH = 100;
    private static final int BOX_HEIGHT = 40;
    private static final int MAX_SIZE = 10; // Max elements in stack visualization

    private final Stack<Integer> stack = new Stack<>();

    @FXML
    private VBox stackContainer;

    @FXML
    private TextField inputField;

    @FXML
    private Button pushButton, popButton, peekButton;

    @FXML
    protected Button backButton;

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        navigateToDashboard();
    }


    @FXML
    public void initialize() {
        pushButton.setOnAction(e -> pushElement());
        popButton.setOnAction(e -> popElement());
        peekButton.setOnAction(e -> peekElement());
    }

    private void pushElement() {
        if (stack.size() >= MAX_SIZE) return; // Prevent overflow

        try {
            int value = Integer.parseInt(inputField.getText());
            stack.push(value);

            // Create a new stack item
            StackPane stackItem = new StackPane();
            Rectangle box = new Rectangle(BOX_WIDTH, BOX_HEIGHT, Color.LIGHTBLUE);
            box.setArcWidth(10);
            box.setArcHeight(10);

            Text text = new Text(String.valueOf(value));
            text.setFill(Color.WHITE);
            text.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

            stackItem.getChildren().addAll(box, text);


            // Add new element to top of stack (at beginning of VBox)
            stackContainer.getChildren().add(0, stackItem);

            // Fade-in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), stackItem);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            inputField.clear();
        } catch (NumberFormatException e) {
            inputField.setText("Invalid");
        }
    }

    private void popElement() {
        if (stack.isEmpty()) return; // Prevent underflow

        stack.pop();

        if (!stackContainer.getChildren().isEmpty()) {
            StackPane topElement = (StackPane) stackContainer.getChildren().get(0);

            // Fade-out animation before removing
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), topElement);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> stackContainer.getChildren().remove(topElement));
            fadeOut.play();
        }
    }

    private void peekElement() {
        if (stack.isEmpty()) return;

        StackPane topElement = (StackPane) stackContainer.getChildren().get(0);
        Rectangle box = (Rectangle) topElement.getChildren().get(0);

        // Highlight effect
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
