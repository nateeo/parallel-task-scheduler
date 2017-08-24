package frontend;

import graph.Edge;
import graph.Graph;
import graph.Node;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.*;

public class GraphDrawer {
    private Graph _graph;
    private Map<Integer, StackPane> shapeMap = new HashMap<>();
    private Map<Integer, Circle> circleMap = new HashMap<>();
    private AnchorPane _graphPane;
    private double _circleSize;

    GraphDrawer(Graph graph, AnchorPane graphPane, double circleSize) {
        _graph = graph;
        _graphPane = graphPane;
        _circleSize = circleSize;
    }

    // This function is used to draw the graph of the input dot file.
    public void drawGraph() {
        List<Node> nodes = _graph.getNodes();
        //List<graph.Node> finishedNodes = new ArrayList<graph.Node>();
        List<Edge> edges = _graph.getEdges();
        List<Node> source = _graph.getStart();
        double graphPaneX = _graphPane.getPrefWidth();
        double graphPaneY = _graphPane.getPrefHeight();

        List<List<Node>> graphLevels = calculateLevels(nodes,edges,source);
        int numberOfLevels = graphLevels.size();
        int maxXSize = 0;
        double maxXSpacing;
        double ySpacing = (graphPaneY - (numberOfLevels * _circleSize)) / (numberOfLevels + 1);
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

        maxXSpacing = (graphPaneX - (maxXSize * _circleSize)) / (maxXSize + 1);

        while(ySpacing <= 0 || maxXSpacing <= 0) {
            _circleSize = _circleSize - 5;
            ySpacing = (graphPaneY - (numberOfLevels * _circleSize)) / (numberOfLevels + 1);
            maxXSpacing = (graphPaneX - (maxXSize * _circleSize)) / (maxXSize + 1);
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
            xSpacing = (graphPaneX - (levels.size() * _circleSize)) / (levels.size() + 1);
            xCoordinate = 0;
            for(Node circlesDrawn: levels) {
                xCoordinate += xSpacing;
                drawCircle(circlesDrawn, xCoordinate, yCoordinate);
                xCoordinate += _circleSize;
            }
            yCoordinate += _circleSize;
        }

        for(Edge edge: edges) {
            StackPane to = shapeMap.get(edge.getTo().getId());
            StackPane from = shapeMap.get(edge.getFrom().getId());
            drawEdges(from, to);
        }
    }

    // Constant values to draw the circle. Set inside a stackPane in order to overlay text.
    public void drawCircle(Node node,double layoutX, double layoutY) {
        Circle circle = new Circle();
        circle.setCenterX(_circleSize);
        circle.setCenterY(_circleSize);
        circle.setRadius(_circleSize/2);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        StackPane stackPane = new StackPane();
        Text text = new Text(node.getName());
        text.setBoundsType(TextBoundsType.VISUAL);
        stackPane.getChildren().addAll(circle,text);
        _graphPane.getChildren().add(stackPane);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        shapeMap.put(node.getId(),stackPane);
        circleMap.put(node.getId(),circle);
    }

    public void drawEdges(StackPane from, StackPane to) {
        double beginningOfLineX = from.getLayoutX() + _circleSize/2;
        double beginningOfLineY = from.getLayoutY() + _circleSize/2;
        double endOfLineX = to.getLayoutX() + _circleSize/2;
        double endOfLineY = to.getLayoutY(); //+ circleSize/2;

        System.out.println(beginningOfLineX+ " " + beginningOfLineY +" " +endOfLineX +" "+  endOfLineY);

        Line line = new Line(beginningOfLineX,beginningOfLineY,endOfLineX,endOfLineY);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(1);
        Line rightArrow = new Line(endOfLineX,endOfLineY,endOfLineX + 5,endOfLineY - 5);
        rightArrow.setStroke(Color.WHITE);
        Line leftArrow = new Line(endOfLineX,endOfLineY, endOfLineX - 5, endOfLineY - 5);
        leftArrow.setStroke(Color.WHITE);
        _graphPane.getChildren().add(0,line);
        _graphPane.getChildren().add(0,rightArrow);
        _graphPane.getChildren().add(0,leftArrow);
    }

    public void handleColorChange(ActionEvent action) {
        System.out.println(shapeMap.keySet());
        circleMap.get(0).setFill(Color.RED);
        //circleMap.get(16).setFill(Color.RED);
        //circleMap.get(2).setFill(Color.RED);
        //circleMap.get(18).setFill(Color.RED);
        //circleMap.get(4).setFill(Color.RED);
        //circleMap.get(6).setFill(Color.RED); // 9
        //circleMap.get(8).setFill(Color.RED); // 1
        //circleMap.get(10).setFill(Color.RED);
        //circleMap.get(12).setFill(Color.RED);
        //circleMap.get(14).setFill(Color.RED);
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
