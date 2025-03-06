package org.example.demo1.controllers.visualizations;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
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
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BSTVisualizationController {
    private BSTNode root;

    @FXML
    private Pane bstPane;

    @FXML
    private TextField inputField;

    @FXML
    private Button insertButton, deleteButton, backButton, searchButton;

    @FXML
    public void initialize() {
        insertButton.setOnAction(e -> insertNode());
        deleteButton.setOnAction(e -> deleteNode());
        searchButton.setOnAction(e -> searchNode());
        backButton.setOnAction(this::onBackButtonClick);
    }

    private void insertNode() {
        try {
            int value = Integer.parseInt(inputField.getText());
            root = insertRecursively(root, value, (int) (bstPane.getWidth() / 2), 50, null, 0);  // Pass depth as 0
            inputField.clear();
        } catch (NumberFormatException e) {
            inputField.setText("Invalid");
        }
    }


    private void searchNode() {
        try {
            int value = Integer.parseInt(inputField.getText());
            highlightSearchPath(root, value);
            inputField.clear();
        } catch (NumberFormatException e) {
            inputField.setText("Invalid");
        }
    }

    private void highlightSearchPath(BSTNode node, int value) {
        List<BSTNode> pathNodes = new ArrayList<>();
        List<Line> pathLines = new ArrayList<>();

        BSTNode current = root;
        while (current != null) {
            pathNodes.add(current);
            if (value < current.value) {
                if (current.leftLine != null) pathLines.add(current.leftLine);
                current = current.left;
            } else if (value > current.value) {
                if (current.rightLine != null) pathLines.add(current.rightLine);
                current = current.right;
            } else {
                break; // Found the node
            }
        }

        boolean isFound = (current != null && current.value == value);
        animateSearchPath(pathNodes, pathLines, isFound);
    }

    private void animateSearchPath(List<BSTNode> nodes, List<Line> lines, boolean isFound) {
        SequentialTransition sequence = new SequentialTransition();

        for (int i = 0; i < nodes.size(); i++) {
            BSTNode node = nodes.get(i);
            Line line = (i < lines.size()) ? lines.get(i) : null;

            PauseTransition highlightNode = new PauseTransition(Duration.seconds(0.5));
            highlightNode.setOnFinished(e -> node.dataBox.setStyle(HIGHLIGHT_STYLE));

            sequence.getChildren().add(highlightNode);

            if (line != null) {
                PauseTransition highlightLine = new PauseTransition(Duration.seconds(0.5));
                highlightLine.setOnFinished(e -> line.setStroke(Color.web("#FBDF82")));
                sequence.getChildren().add(highlightLine);
            }
        }

        sequence.setOnFinished(e -> {
            if (isFound) {
                displayMessage("Element Found!", Color.GREEN);
            } else {
                displayMessage("Element Not Found!", Color.RED);
            }

            PauseTransition resetColors = new PauseTransition(Duration.seconds(2));
            resetColors.setOnFinished(ev -> resetTreeColors(root));
            resetColors.play();
        });

        sequence.play();
    }

    private void resetTreeColors(BSTNode node) {
        if (node == null) return;

        node.dataBox.setStyle(DEFAULT_STYLE);

        if (node.leftLine != null) node.leftLine.setStroke(Color.web("#cb64ec"));
        if (node.rightLine != null) node.rightLine.setStroke(Color.web("#cb64ec"));

        resetTreeColors(node.left);
        resetTreeColors(node.right);
    }

    private void displayMessage(String message, Color color) {
        Text text = new Text(message);
        text.setFont(Font.font("Arial", 18));
        text.setFill(color);
        text.setX(bstPane.getWidth() / 2 - 50);
        text.setY(bstPane.getHeight() - 30);
        bstPane.getChildren().add(text);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> bstPane.getChildren().remove(text));
        delay.play();
    }

    private static final String HIGHLIGHT_STYLE = "-fx-background-color: #FBDF82; " +
            "-fx-background-radius: 50%; " +  // Makes it a perfect circle
            "-fx-border-radius: 50%; " +  // Ensures border follows the circle shape
            "-fx-border-color: transparent; " +  // Removes the border
            "-fx-font-size: 22px; " +
            "-fx-font-weight: bold; " +
            "-fx-alignment: center; " +  // Keep text properly aligned
            "-fx-padding: 0; " +  // Ensures text does not get distorted
            "-fx-min-width: 60px; " +  // Fix width to maintain circular shape
            "-fx-min-height: 60px; " +  // Fix height to maintain circular shape
            "-fx-max-width: 60px; " +
            "-fx-text-fill: white;" +
            "-fx-max-height: 60px; ";

    private static final String DEFAULT_STYLE = "-fx-background-color: #cb64ec; " +
            "-fx-background-radius: 50%; " +  // Makes it a perfect circle
            "-fx-border-radius: 50%; " +  // Ensures border follows the circle shape
            "-fx-border-color: transparent; " +  // Removes the border
            "-fx-font-size: 22px; " +
            "-fx-font-weight: bold; " +
            "-fx-alignment: center; " +  // Keep text properly aligned
            "-fx-padding: 0; " +  // Ensures text does not get distorted
            "-fx-min-width: 60px; " +  // Fix width to maintain circular shape
            "-fx-min-height: 60px; " +  // Fix height to maintain circular shape
            "-fx-max-width: 60px; " +
            "-fx-text-fill: white;" +
            "-fx-max-height: 60px; ";

    private BSTNode insertRecursively(BSTNode root, int value, int x, int y, BSTNode parent, int depth) {
        int horizontalSpacing = 180 - (depth * 50); // Increase spacing with depth
        int verticalSpacing = 120 + (depth * 15);; // Increase vertical distance between levels

        if (root == null) {
            root = new BSTNode(value, x, y);
            bstPane.getChildren().add(root.group);

            if (parent != null) {
                double parentX = parent.x + 30;
                double parentY = parent.y + 60;
                double childX = x + 30;
                double childY = y;

                Line line = new Line(parentX, parentY, childX, childY);
                line.setStrokeWidth(2);
                line.setStroke(Color.web("#cb64ec"));
                bstPane.getChildren().add(line);

                if (value < parent.value) parent.leftLine = line;
                else parent.rightLine = line;

                root.lineToParent = line;
            }
            return root;
        }

        if (value < root.value) {
            root.left = insertRecursively(root.left, value, x - horizontalSpacing, y + verticalSpacing, root, depth + 1);
        } else if (value > root.value) {
            root.right = insertRecursively(root.right, value, x + horizontalSpacing, y + verticalSpacing, root, depth + 1);
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
            if (root.left == null && root.right == null) {
                bstPane.getChildren().remove(root.group);
                if (root.lineToParent != null) bstPane.getChildren().remove(root.lineToParent);
                return null;
            }

            if (root.right != null) {
                root.value = root.right.value;
                updateVisualNode(root);
                bstPane.getChildren().remove(root.right.group);
                if (root.right.lineToParent != null) bstPane.getChildren().remove(root.right.lineToParent);
                root.right = deleteRecursively(root.right, root.right.value);
            } else if (root.left != null) {
                root.value = root.left.value;
                updateVisualNode(root);
                bstPane.getChildren().remove(root.left.group);
                if (root.left.lineToParent != null) bstPane.getChildren().remove(root.left.lineToParent);
                root.left = deleteRecursively(root.left, root.left.value);
            }
        }
        return root;
    }

    private void updateVisualNode(BSTNode node) {
        if (node != null) {
            node.dataBox.setText(String.valueOf(node.value));
        }
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
        }
    }

    private static class BSTNode {
        int value;
        BSTNode left, right;
        int x, y;
        Group group;
        TextField dataBox;
        Line lineToParent;
        Line leftLine, rightLine;

        public BSTNode(int value, int x, int y) {
            this.value = value;
            this.x = x;
            this.y = y;

            dataBox = new TextField(String.valueOf(value));
            dataBox.setPrefSize(60, 60);
            dataBox.setStyle(
                    "-fx-background-color: #cb64ec; " +  // Fully blue background
                            "-fx-background-radius: 50%; " +  // Makes it a perfect circle
                            "-fx-border-radius: 50%; " +  // Ensures border follows the circle shape
                            "-fx-border-color: transparent; " +  // Removes the border
                            "-fx-font-size: 22px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-alignment: center; " +  // Keep text properly aligned
                            "-fx-padding: 0; " +  // Ensures text does not get distorted
                            "-fx-min-width: 60px; " +  // Fix width to maintain circular shape
                            "-fx-min-height: 60px; " +  // Fix height to maintain circular shape
                            "-fx-max-width: 60px; " +
                            "-fx-max-height: 60px; " +
                            "-fx-text-fill: white;"
            );

            dataBox.setEditable(false);

            this.group = new Group(dataBox);
            this.group.setLayoutX(x);
            this.group.setLayoutY(y);
        }
    }

}
