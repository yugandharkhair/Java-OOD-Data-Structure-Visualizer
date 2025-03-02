package org.example.demo1.controllers.visualizations;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class BSTVisualizationController {
    private BSTNode root;

    @FXML
    private Pane bstPane;

    @FXML
    private TextField inputField;

    @FXML
    private Button insertButton, deleteButton, backButton;

    @FXML
    public void initialize() {
        insertButton.setOnAction(e -> insertNode());
        deleteButton.setOnAction(e -> deleteNode());
        backButton.setOnAction(this::onBackButtonClick);
    }

    private void insertNode() {
        try {
            int value = Integer.parseInt(inputField.getText());
            root = insertRecursively(root, value, (int) (bstPane.getWidth() / 2), 50, null);
            inputField.clear();
        } catch (NumberFormatException e) {
            inputField.setText("Invalid");
        }
    }

    private BSTNode insertRecursively(BSTNode root, int value, int x, int y, BSTNode parent) {
        if (root == null) {
            root = new BSTNode(value, x, y);
            bstPane.getChildren().add(root.group);

            if (parent != null) {
                // 🔹 New Adjusted Line Placement 🔹
                double parentX = parent.x + 30; // Adjust to center
                double parentY = parent.y + 60; // Start from bottom of parent box
                double childX = x + 30;         // Center of child box
                double childY = y - 0;         // Connect to top of child box

                Line line = new Line(parentX, parentY, childX, childY);
                line.setStrokeWidth(2);
                line.setStroke(Color.LIGHTBLUE); // Use blue color scheme
                bstPane.getChildren().add(line);

                root.lineToParent = line;  // 🔥 Store reference for deletion
            }

            return root;
        }

        if (value < root.value) {
            root.left = insertRecursively(root.left, value, x - 100, y + 80, root);
        } else if (value > root.value) {
            root.right = insertRecursively(root.right, value, x + 100, y + 80, root);
        }

        return root;
    }




    private void deleteNode() {
        try {
            int value = Integer.parseInt(inputField.getText());
            root = deleteRecursively(root, value);
            inputField.clear();
        } catch (NumberFormatException e) {
            inputField.setText("Invalid");
        }
    }

    private BSTNode deleteRecursively(BSTNode root, int value) {
        if (root == null) return null;

        if (value < root.value) {
            root.left = deleteRecursively(root.left, value);
        } else if (value > root.value) {
            root.right = deleteRecursively(root.right, value);
        } else {
            // 🔹 Case 1: No children - Remove the node and its line
            if (root.left == null && root.right == null) {
                bstPane.getChildren().remove(root.group);
                if (root.lineToParent != null) bstPane.getChildren().remove(root.lineToParent);
                return null;
            }

            // 🔹 Case 2: One child - Replace value instead of removing
            if (root.right != null) {
                root.value = root.right.value;  // Replace parent value with right child
                updateVisualNode(root); // Update visual node with new value

                // Remove the right child's visualization
                bstPane.getChildren().remove(root.right.group);
                if (root.right.lineToParent != null) bstPane.getChildren().remove(root.right.lineToParent);

                root.right = deleteRecursively(root.right, root.right.value);
            } else if (root.left != null) {
                root.value = root.left.value;  // Replace parent value with left child
                updateVisualNode(root); // Update visual node with new value

                // Remove the left child's visualization
                bstPane.getChildren().remove(root.left.group);
                if (root.left.lineToParent != null) bstPane.getChildren().remove(root.left.lineToParent);

                root.left = deleteRecursively(root.left, root.left.value);
            }
        }
        return root;
    }

    private void updateVisualNode(BSTNode node) {
        if (node != null) {
            node.dataBox.setText(String.valueOf(node.value)); // Set new value in the TextField
        }
    }





    private int minValue(BSTNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root.value;
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) {
        navigateToScreen("/org/example/demo1/visualization.fxml");
    }

    private void navigateToScreen(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class BSTNode {
        int value;
        BSTNode left, right;
        int x, y;
        Group group;
        TextField dataBox; // 🔥 Replace Circle with a TextField
        Line lineToParent; // 🔥 Store reference to the parent connection

        public BSTNode(int value, int x, int y) {
            this.value = value;
            this.x = x;
            this.y = y;

            // 🔵 Create a Data Box (Rounded TextField)
            dataBox = new TextField(String.valueOf(value));
            dataBox.setPrefSize(60, 60); // Set width and height
            dataBox.setStyle("-fx-background-color: LIGHTBLUE; " + // Blue background
                    "-fx-border-color: transparent; " +         // Black border
                    "-fx-border-radius: 50%; " +           // Rounded corners
                    "-fx-font-size: 18px; " +             // Font size
                    "-fx-font-weight: bold; " +           // Bold text
                    "-fx-text-fill: white; " +            // White text color
                    "-fx-alignment: center;");            // Center text alignment
            dataBox.setEditable(false); // 🔒 Make text non-editable

            // Create Group for rendering
            this.group = new Group(dataBox);
            this.group.setLayoutX(x);
            this.group.setLayoutY(y);
        }
    }



}
