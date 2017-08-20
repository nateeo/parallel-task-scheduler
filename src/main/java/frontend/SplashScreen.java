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


public class SplashScreen implements Initializable {

    Graph _graph;
    Map<String, Circle> shapeMap = new HashMap<String, Circle>();

    @FXML
    private AnchorPane graphPane;

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

    public void drawGraph(Graph graph) {
        List<graph.Node> nodes = graph.getNodes();
        List<graph.Node> finishedNodes = new ArrayList<graph.Node>();
        List<Edge> edges = graph.getEdges();
        List<Node> source = graph.getStart();
        int increment = 10;

        for(graph.Node node: nodes) {
            System.out.println(node.getName());
            Circle circle = new Circle();
            circle.setCenterX(30);
            circle.setCenterY(30);
            circle.setRadius(15);
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.BLACK);
            StackPane stackPane = new StackPane();
            Text text = new Text(node.getName());
            text.setBoundsType(TextBoundsType.VISUAL);
            stackPane.getChildren().addAll(circle,text);
            graphPane.getChildren().add(stackPane);
            stackPane.setLayoutX(increment);
            increment = increment + 40;
        }
    }

    public List<List<Node>> calculateLevels(List<Node> nodes, List<Edge> edges, List<Node> source) {
        Queue<Node> queuedNodes = new LinkedList<Node>();
        List<List<Node>> returnList = new ArrayList<>();
        //HashMap<Node,>
        returnList.add(source);
        Node currentNode;
        int level;
        int currentLevel;

        // Adds all the source nodes into the Queue.
        for(Node node: source) {
            queuedNodes.add(node);
        }

        while(!queuedNodes.isEmpty()) {
            currentNode = queuedNodes.remove();
            level = 0;
            // If there are incoming edges on the node, this is for not source
            if(!currentNode.getIncoming().isEmpty()) {
                for(Edge predecessors: currentNode.getIncoming()) {
                    //currentLevel =
                }
            }
        }
        return returnList;
    }

}


