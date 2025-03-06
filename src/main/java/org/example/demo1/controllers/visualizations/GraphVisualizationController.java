package org.example.demo1.controllers.visualizations;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphVisualizationController {

    @FXML
    private Pane graphPane;

    @FXML
    private Text bfsLabel;

    @FXML
    private Button backButton, bfsButton, dfsButton;

    @FXML
    private CheckBox arrowCheckbox; // Checkbox to toggle arrow visibility

    @FXML
    private CheckBox weightedGraphCheckbox;

    private List<GraphNode> nodes = new ArrayList<>();
    private static final String LETTERS = "CAFDEBG"; // Order: 0→1→2→3→4→5→6

    @FXML
    public void initialize() {
        createFixedGraph();
        backButton.setOnAction(this::onBackButtonClick);
        bfsButton.setOnAction(e -> visualizeBFS());
        dfsButton.setOnAction(e -> visualizeDFS());
        arrowCheckbox.setOnAction(e -> updateGraphArrows());
    }

    private void createFixedGraph() {
        // Define positions for nodes (DAG-like layout)
        double[][] positions = {
                {300, 50},  // C (Root)
                {100, 150}, {500, 150}, // A, F
                {25, 300}, {175, 300}, {500, 300}, // D, E, B
                {350, 350} // G
        };

        // Create nodes
        for (int i = 0; i < positions.length; i++) {
            addNode(positions[i][0], positions[i][1], LETTERS.charAt(i));
        }

        // Create directed edges (arrows)
        addDirectedEdge(nodes.get(1), nodes.get(0)); // A → C
        addDirectedEdge(nodes.get(0), nodes.get(2)); // C → F
        addDirectedEdge(nodes.get(0), nodes.get(6)); // C → G
        addDirectedEdge(nodes.get(0), nodes.get(4)); // C → E
        addDirectedEdge(nodes.get(3), nodes.get(1)); // D → A
        addDirectedEdge(nodes.get(4), nodes.get(1)); // E → A
        addDirectedEdge(nodes.get(3), nodes.get(4)); // D → E
        addDirectedEdge(nodes.get(2), nodes.get(5)); // B → F
        addDirectedEdge(nodes.get(5), nodes.get(0)); // B → C
    }

    private void addNode(double x, double y, char name) {
        GraphNode node = new GraphNode(x, y, String.valueOf(name));
        nodes.add(node);
        graphPane.getChildren().addAll(node.circle, node.label);
    }

    private void updateGraphArrows() {
        graphPane.getChildren().clear(); // Clear all edges & arrows
        for (GraphNode node : nodes) {
            graphPane.getChildren().addAll(node.circle, node.label); // Re-add nodes
        }

        //Recreate edges (arrows depend on checkbox state)
        addDirectedEdge(nodes.get(1), nodes.get(0));
        addDirectedEdge(nodes.get(0), nodes.get(2));
        addDirectedEdge(nodes.get(0), nodes.get(6));
        addDirectedEdge(nodes.get(0), nodes.get(4));
        addDirectedEdge(nodes.get(3), nodes.get(1));
        addDirectedEdge(nodes.get(4), nodes.get(1));
        addDirectedEdge(nodes.get(3), nodes.get(4));
        addDirectedEdge(nodes.get(2), nodes.get(5));
        addDirectedEdge(nodes.get(5), nodes.get(0));
    }

    private void addDirectedEdge(GraphNode node1, GraphNode node2) {
        double radius = node1.circle.getRadius(); // Assume both circles have the same radius

        // Compute direction vector
        double dx = node2.circle.getCenterX() - node1.circle.getCenterX();
        double dy = node2.circle.getCenterY() - node1.circle.getCenterY();
        double length = Math.sqrt(dx * dx + dy * dy);

        // Normalize the direction vector
        double unitDx = dx / length;
        double unitDy = dy / length;

        // Adjust start and end points to be at the circle's perimeter
        double startX = node1.circle.getCenterX() + unitDx * radius;
        double startY = node1.circle.getCenterY() + unitDy * radius;
        double endX = node2.circle.getCenterX() - unitDx * radius;
        double endY = node2.circle.getCenterY() - unitDy * radius;

        // Create the adjusted edge
        Line edge = new Line(startX, startY, endX, endY);
        edge.setStrokeWidth(2);
        edge.setStroke(Color.BLACK);

        int weight = new Random().nextInt(9) + 1;
        // **Positioning Weight Label Correctly**
        double weightX = (startX + endX) / 2 + (-unitDy * 15);  // Adjusted perpendicular offset
        double weightY = (startY + endY) / 2 + (unitDx * 15);   // Adjusted perpendicular offset

        Text weightLabel = new Text(String.valueOf(weight));
        weightLabel.setFill(Color.BLACK);
        weightLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        weightLabel.setX(weightX);
        weightLabel.setY(weightY);

        // Initially set visibility based on checkbox state
        weightLabel.setVisible(weightedGraphCheckbox.isSelected());

        // Ensure weight labels update when the checkbox state changes
        weightedGraphCheckbox.setOnAction(e -> {
            for (javafx.scene.Node node : graphPane.getChildren()) {
                if (node instanceof Text && !((Text) node).getText().matches("[A-Z]")) {
                    node.setVisible(weightedGraphCheckbox.isSelected());
                }
            }
        });

        // Add elements to graphPane
        graphPane.getChildren().addAll(edge, weightLabel);


        // Add arrowhead ONLY if checkbox is checked
        if (arrowCheckbox.isSelected()) {
            Polygon arrowHead = createArrowHead(endX, endY, unitDx, unitDy);
            graphPane.getChildren().add(arrowHead);
        }
    }


    private Polygon createArrowHead(double x, double y, double unitDx, double unitDy) {
        double arrowLength = 10;
        double arrowAngle = Math.PI / 6;

        double x1 = x - arrowLength * Math.cos(Math.atan2(unitDy, unitDx) - arrowAngle);
        double y1 = y - arrowLength * Math.sin(Math.atan2(unitDy, unitDx) - arrowAngle);
        double x2 = x - arrowLength * Math.cos(Math.atan2(unitDy, unitDx) + arrowAngle);
        double y2 = y - arrowLength * Math.sin(Math.atan2(unitDy, unitDx) + arrowAngle);

        Polygon arrowHead = new Polygon(x, y, x1, y1, x2, y2);
        arrowHead.setFill(Color.BLACK);
        return arrowHead;
    }

    private void visualizeBFS() {
        int[] bfsOrder = {3, 1, 4, 0, 2, 6, 5}; // BFS order
        String letters = "DAECFGB";
        highlightNodesInOrder(bfsOrder, letters);
    }

    private void visualizeDFS() {
        int[] dfsOrder = {3, 1, 0, 2, 5, 6, 4}; // DFS order
        String letters = "DACFBGE";
        highlightNodesInOrder(dfsOrder, letters);
    }

    private void highlightNodesInOrder(int[] order, String traversalOrder) {
        bfsLabel.setText("");
        PauseTransition[] transitions = new PauseTransition[order.length];

        for (int i = 0; i < order.length; i++) {
            GraphNode node = nodes.get(order[i]);
            char letter = traversalOrder.charAt(i);

            transitions[i] = new PauseTransition(Duration.seconds(i * 0.7));
            transitions[i].setOnFinished(e -> {
                node.circle.setFill(Color.web("#FBDF82"));
                bfsLabel.setText(bfsLabel.getText() + " " + letter);
            });
        }

        PauseTransition reset = new PauseTransition(Duration.seconds(3));
        reset.setOnFinished(ev -> resetColors());
        transitions[order.length - 1].setOnFinished(e -> {
            nodes.get(order[order.length - 1]).circle.setFill(Color.web("#FBDF82"));
            bfsLabel.setText(bfsLabel.getText() + " " + traversalOrder.charAt(order.length - 1));
            reset.play(); // Start reset after highlighting last node
        });

        for (PauseTransition transition : transitions) {
            transition.play();
        }
    }

    private void resetColors() {
        for (GraphNode node : nodes) {
            node.circle.setFill(Color.LIGHTGREEN);
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

    private static class GraphNode {
        Circle circle;
        Text label;

        public GraphNode(double x, double y, String name) {
            circle = new Circle(x, y, 30, Color.LIGHTGREEN);
            circle.setStroke(null);
            circle.setStrokeWidth(0);
            label = new Text(name);
            label.setFill(Color.BLACK);
            label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            label.setX(x - 6);
            label.setY(y + 6);
        }
    }
}
