package frontend;

import graph.Edge;
import graph.Graph;
import graph.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * The splash Screen is meant to provide visual feedback to the user while it waits for the Task Schedular algorithm to complete
 * running.
 *
 * Author: Samule Li, Edison Rho
 */

public class SplashScreen implements Initializable {

    Graph _graph;
    Map<Integer, Circle> shapeMap = new HashMap<>();

    @FXML
    private AnchorPane graphPane;
    private double circleSize = 40;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _graph = Main.getGraph();
        drawGraph(_graph);

    }

    public void changeScene(String filePathToXML, ActionEvent actionEvent) throws Exception{
        URL url = new File(filePathToXML).toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene nextScene = new Scene(root);
        Stage loadingPage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        loadingPage.setScene(nextScene);
    }

    // This function is used to draw the graph of the input dot file.
    public void drawGraph(Graph graph) {
        List<graph.Node> nodes = graph.getNodes();
        //List<graph.Node> finishedNodes = new ArrayList<graph.Node>();
        List<Edge> edges = graph.getEdges();
        List<Node> source = graph.getStart();
        double graphPaneX = graphPane.getPrefWidth();
        double graphPaneY = graphPane.getPrefHeight();

        List<List<Node>> graphLevels = calculateLevels(nodes,edges,source);
        int numberOfLevels = graphLevels.size();
        int maxXSize = 0;
        double maxXSpacing;
        double ySpacing = (graphPaneY - (numberOfLevels * circleSize)) / (numberOfLevels + 1);
        double xSpacing;
        double yCoordinate = 0;
        double xCoordinate;

        // This calculates the maximum size of the X axis, this is for resizing the graph nodes in case they
        // do not all fit inside their allocated pane.
        for(List<Node> calculateSize: graphLevels) {
            if(calculateSize.size() > maxXSize) {
                maxXSize = calculateSize.size();
            }
        }

        maxXSpacing = (graphPaneX - (maxXSize * circleSize)) / (maxXSize + 1);

        while(ySpacing <= 0 || maxXSpacing <= 0) {
            circleSize = circleSize - 5;
            ySpacing = (graphPaneY - (numberOfLevels * circleSize)) / (numberOfLevels + 1);
            maxXSpacing = (graphPaneX - (maxXSize * circleSize)) / (maxXSize + 1);
        }

        for(int i = 0 ; i < graphLevels.size(); i++) {
            String printShit = "Level [" +i+"]";
            for(int j = 0 ; j < graphLevels.get(i).size(); j++) {
                printShit += graphLevels.get(i).get(j);
            }
            System.out.println(printShit);
        }

        int i = 0;

        for (List<Node> levels: graphLevels) {

            yCoordinate += ySpacing;
            System.out.println("ySpacing: " +ySpacing +" For level" + i);
            i++;
            System.out.println("Y Coordinate: "+yCoordinate);
            xSpacing = (graphPaneX - (levels.size() * circleSize)) / (levels.size() + 1);
            xCoordinate = 0;
            for(Node circlesDrawn: levels) {
                xCoordinate += xSpacing;
                drawCircle(circlesDrawn, xCoordinate, yCoordinate);
                xCoordinate += circleSize;
            }
            yCoordinate += circleSize;
        }
    }

    // Constant values to draw the circle. Set inside a stackPane in order to overlay text.
    public void drawCircle(Node node,double layoutX, double layoutY) {
        Circle circle = new Circle();
        circle.setCenterX(circleSize);
        circle.setCenterY(circleSize);
        circle.setRadius(circleSize/2);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        StackPane stackPane = new StackPane();
        Text text = new Text(node.getName());
        text.setBoundsType(TextBoundsType.VISUAL);
        stackPane.getChildren().addAll(circle,text);
        graphPane.getChildren().add(stackPane);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        shapeMap.put(node.getId(),circle);
    }

    public void drawEdges() {

    }

    public void handleColorChange(ActionEvent action) {
        System.out.println(shapeMap.keySet());
        shapeMap.get(0).setFill(Color.RED);
    }

    // This function is used to calculate the levels of each node, depending on the maximum level of it's parent
    // nodes. If the node has two parent nodes at levels 1 and 3, the level of the current node would be equivalent
    // to 4.

    public List<List<Node>> calculateLevels(List<Node> nodes, List<Edge> edges, List<Node> source) {
        Queue<Node> queuedNodes = new LinkedList<Node>();
        List<List<Node>> returnList = new ArrayList<>();
        Map<Integer,Integer> levelsMap = new HashMap<>();
        returnList.add(source);
        Node currentNode;
        Node successorNode;
        int level;
        int currentLevel;
        boolean allPredecessorsCalculated;

        // Adds all the source nodes into the Queue.
        for(Node node: source) {
            queuedNodes.add(node);
            levelsMap.put(node.getId(),0);
        }

        while(!queuedNodes.isEmpty()) {
            currentNode = queuedNodes.remove();
            level = 0;
            // If there are incoming edges on the node, this is for not source
            if(!currentNode.getIncoming().isEmpty()) {
                for(Edge predecessors: currentNode.getIncoming()) {
                    currentLevel = levelsMap.get(predecessors.getFrom().getId());
                    if(currentLevel > level) {
                        level = currentLevel;
                    }
                }
                level = level + 1;
                levelsMap.put(currentNode.getId(),level);

                // if the level is higher or equal to the returnList size then create a new arrayList. This is
                // adding the currentNode to the correct level array.
                if(level >= returnList.size()) {
                    List<Node> newNodeList = new ArrayList<>();
                    newNodeList.add(currentNode);
                    returnList.add(newNodeList);
                }
                else {
                    returnList.get(level).add(currentNode);
                }
            }

            // Adding nodes to the queue if all predecessors levels have been computed.
            if(!currentNode.getOutgoing().isEmpty()) {
                for(Edge successors : currentNode.getOutgoing()) {
                    successorNode = successors.getTo();
                    allPredecessorsCalculated = true;

                    for(Edge sPredecssors : successorNode.getIncoming()) {
                        if(!levelsMap.containsKey(sPredecssors.getFrom().getId())) {
                            allPredecessorsCalculated = false;
                        }
                    }
                    if (allPredecessorsCalculated) {
                        queuedNodes.add(successorNode);
                    }
                }
            }
        }
        return returnList;
    }

}


