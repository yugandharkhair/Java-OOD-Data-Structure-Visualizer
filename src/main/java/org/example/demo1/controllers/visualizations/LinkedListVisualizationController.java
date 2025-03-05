package org.example.demo1.controllers.visualizations;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

public class LinkedListVisualizationController {
    private static final int BOX_WIDTH = 60;
    private static final int BOX_HEIGHT = 50;

    private final LinkedList<HBox> nodeList = new LinkedList<>();

    @FXML
    private Pane linkedListPane; // The container for visualization

    @FXML
    private TextField positionField; // Input for position

    @FXML
    private TextField inputField;

    @FXML
    private Button insertButton, deleteFirstButton,deleteLastButton, deletePositionButton, backButton;

    private double currentX = 50; // Start position for first node

    private static final int MAX_NODES = 9;

    @FXML
    public void initialize() {
        insertButton.setOnAction(e -> insertNode());
        deleteFirstButton.setOnAction(e -> deleteFirstNode());
        deleteLastButton.setOnAction(e -> deleteLastNode());
        deletePositionButton.setOnAction(e -> {
                    try {
                        int position = Integer.parseInt(positionField.getText());
                        deleteAtPosition(position);
                        positionField.clear(); // Clear the input after action
                    } catch (NumberFormatException ex) {
                        positionField.setText("Invalid");
                    }
                });
        backButton.setOnAction(this::onBackButtonClick);

        // Initialize with 2 empty boxes
        addInitialEmptyBoxes();
    }

    private void addInitialEmptyBoxes() {
        HBox initialBox = new HBox(0); // No spacing

        // Empty Box 1
        TextField emptyBox1 = new TextField("Head");
        emptyBox1.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
        emptyBox1.setStyle("-fx-background-color: white; -fx-font-weight: bold; -fx-border-color: black; -fx-font-size: 16px; -fx-alignment: center;");

        // Empty Box 2
        TextField emptyBox2 = new TextField("--->");
        emptyBox2.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
        emptyBox2.setStyle("-fx-background-color: white; -fx-font-weight: bold; -fx-border-color: transparent; -fx-font-size: 16px; -fx-alignment: center;");

        // Add them together
        initialBox.getChildren().addAll(emptyBox1, emptyBox2);

        // Add to the pane
        linkedListPane.getChildren().add(initialBox);
        initialBox.setLayoutX(currentX);
        initialBox.setLayoutY(100);

        // Update starting position for actual nodes
        currentX += (BOX_WIDTH * 2);
    }

    private void insertNode() {
        if (nodeList.size() >= MAX_NODES) return;
        String value = inputField.getText().trim();
        if (value.isEmpty()) return;

        // Create a node container (HBox to hold 3 text boxes)
        HBox nodeContainer = new HBox(0); // No spacing

        // Data Box (Editable & Blue)
        TextField dataBox = new TextField(value);
        dataBox.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
        dataBox.setStyle("-fx-background-color: #7c4dff; -fx-border-color: black; -fx-font-size: 22px; -fx-font-weight: bold; -fx-alignment: center; -fx-text-fill: white;");

        // Pointer Box 1 (White & Empty)
        TextField pointerBox1 = new TextField("Next");
        pointerBox1.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
        pointerBox1.setStyle("-fx-background-color: white; -fx-font-weight: bold; -fx-border-color: black black black transparent; -fx-font-size: 16px; -fx-alignment: center;");

        // Pointer Box 2 (White & Empty)
        TextField pointerBox2 = new TextField("--->");
        pointerBox2.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
        pointerBox2.setStyle("-fx-background-color: white; -fx-font-weight: bold; -fx-border-color: transparent; -fx-font-size: 16px; -fx-alignment: center;");

        TextField emptyBox3 = new TextField("Null");
        emptyBox3.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
        emptyBox3.setStyle("-fx-background-color: white; -fx-font-weight: bold; -fx-border-color: black; -fx-font-size: 16px; -fx-alignment: center;");

        // Add all boxes together
        nodeContainer.getChildren().addAll(dataBox, pointerBox1, pointerBox2, emptyBox3);

        // Add node to pane
        nodeList.add(nodeContainer);
        linkedListPane.getChildren().add(nodeContainer);

        // Position node correctly
        nodeContainer.setLayoutX(currentX);
        nodeContainer.setLayoutY(100);

        // Move to next position for the new node
        currentX += (BOX_WIDTH * 3); // Each node set has 3 boxes

        inputField.clear();
    }

    private void deleteFirstNode() {
        if (nodeList.isEmpty()) return;

        // Remove the first inserted node (FIFO behavior)
        HBox firstNode = nodeList.removeFirst();
        linkedListPane.getChildren().remove(firstNode);

        // Shift nodes left after deletion
        currentX -= (BOX_WIDTH * 3);
        repositionNodes();
    }

    private void deleteLastNode() {
        if (nodeList.isEmpty()) return;

        // Remove the last inserted node (LIFO behavior)
        HBox lastNode = nodeList.removeLast();
        linkedListPane.getChildren().remove(lastNode);

        currentX -= (BOX_WIDTH * 3);
    }

    private void deleteAtPosition(int position) {
        if (nodeList.isEmpty() || position < 1 || position > nodeList.size()) {
            System.out.println("Invalid position or empty list.");
            return;
        }

        // Get the node at the specified position (1-based index)
        HBox nodeToRemove = nodeList.remove(position - 1);
        linkedListPane.getChildren().remove(nodeToRemove);

        // Shift remaining nodes left
        currentX -= (BOX_WIDTH * 3);
        repositionNodes();
    }


    private void repositionNodes() {
        double newX = 50 + (BOX_WIDTH * 2); // Keep space for initial empty boxes
        for (HBox node : nodeList) {
            node.setLayoutX(newX);
            newX += (BOX_WIDTH * 3);
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
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
