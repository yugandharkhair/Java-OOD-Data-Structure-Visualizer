package org.example.demo1.controllers.visualizations;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class ArrayVisualizationController {
    private static final int BOX_SIZE = 60;
    private int maxSize = 0;  // Dynamically set max size
    private int currentSize = 0;
    private final ArrayList<Integer> array = new ArrayList<>();

    @FXML
    private HBox arrayBox, indexBox; // HBoxes for displaying the array and indexes

    @FXML
    private TextField inputField, sizeInputField;

    @FXML
    private Button insertButton, removeButton, setSizeButton;

    @FXML
    public void initialize() {
        insertButton.setOnAction(e -> insertElement());
        removeButton.setOnAction(e -> removeElement());
        setSizeButton.setOnAction(e -> onSetSizeClick());
    }

    @FXML
    protected Button backButton;

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        navigateToDashboard();
    }

    @FXML
    private void onSetSizeClick() {
        try {
            int size = Integer.parseInt(sizeInputField.getText());
            if (size <= 0) {
                sizeInputField.setText("Invalid");
                return;
            }

            this.maxSize = size;
            this.currentSize = 0;
            array.clear();
            arrayBox.getChildren().clear();

            // Create empty text fields styled as blue boxes with indexes below them
            for (int i = 0; i < maxSize; i++) {
                TextField textField = new TextField();
                textField.setPrefSize(60, 60);
                textField.setEditable(false); // Prevent manual input
                textField.setStyle("-fx-background-color: lightblue; -fx-font-size: 18px; -fx-alignment: center; -fx-font-weight: bold;");

                Text indexText = new Text(String.valueOf(i));
                indexText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                VBox cell = new VBox(textField, indexText);
                cell.setAlignment(javafx.geometry.Pos.CENTER);
                arrayBox.getChildren().add(cell);
            }

            sizeInputField.clear();
        } catch (NumberFormatException e) {
            sizeInputField.setText("Invalid");
        }
    }

    private void insertElement() {
        if (currentSize >= maxSize) return; // Prevent overflow

        try {
            int value = Integer.parseInt(inputField.getText());
            array.add(value);

            // Get the VBox for the current index
            VBox cell = (VBox) arrayBox.getChildren().get(currentSize);
            TextField textField = (TextField) cell.getChildren().get(0);

            textField.setText(String.valueOf(value)); // Display inserted value

            // Fade-in effect for smooth insertion
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), textField);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            currentSize++;
            inputField.clear();
        } catch (NumberFormatException e) {
            inputField.setText("Invalid");
        }
    }

    private void removeElement() {
        if (currentSize == 0) return; // Prevent underflow

        currentSize--;

        // Get the VBox for the last filled index
        VBox cell = (VBox) arrayBox.getChildren().get(currentSize);
        TextField textField = (TextField) cell.getChildren().get(0);

        // Fade-out effect before clearing the value
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), textField);
        fadeOut.setFromValue(1);
        fadeOut.setOnFinished(e -> textField.setText("")); // Clear text after fade
        fadeOut.play();
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
