package frontend;

import algorithm.PSManager;
import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import graph.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;

import graph.Graph;

import static dotParser.Parser.parseDotFile;

public class Main extends Application {

    static Graph _graph;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // input the graph.
        File testGraph = new File ("input-graphs/Nodes_10_Fork_Join.dot");
        _graph = parseDotFile(testGraph);
        List<Node> listOfNodes = _graph.getNodes();

        URL url = new File("src/main/java/frontend/SplashScreen.fxml").toURI().toURL();
        //Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Welcome to Hi-5 Scheduling");


        //TESTING-START

        PSPriorityQueue priorityQueue = new PSPriorityQueue(_graph, 3);

        // PSManager instance to perform calculations and generate states from existing Partial Solutions
        PartialSolution ps = null;
        PSManager psManager = new PSManager(3, _graph);
        //priority queue will terminate upon the first instance of a total solution
        while (priorityQueue.hasNext()) {
            ps = priorityQueue.getCurrentPartialSolution();
            //generate the child partial solutions from the current "best" candidate partial solution
            //then add to the priority queue based on conditions.
            psManager.generateChildren(ps, priorityQueue);
        }
        ps = priorityQueue.getCurrentPartialSolution();

        ScheduleGraph sgm = new ScheduleGraph(ps);


        Pane root = sgm.generateGraph(ps);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);


        //TESTING-END
        primaryStage.show();
    }

    public static Graph getGraph(){
        return _graph;
    }

    public static void main(String[] args) {
        launch(args);
    }

    //private File testGraph = new File ("../../../test/resources/exampleSmall.dot");
    //private Graph graph = parseDotFile(testGraph);
}
