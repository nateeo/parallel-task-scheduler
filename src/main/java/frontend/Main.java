package frontend;

import algorithm.PSManager;
import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import graph.Node;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import graph.Graph;
import javafx.util.Duration;
import scheduler.Scheduler;

import static dotParser.Parser.parseDotFile;
import static javafx.animation.Animation.INDEFINITE;

public class Main extends Application {

    static Graph _graph;
    static PartialSolution _ps;

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*// input the graph.
        File testGraph = new File ("src/test/resources/exampleLarge.dot");
        _graph = parseDotFile(testGraph);
        List<Node> listOfNodes = _graph.getNodes();*/

        URL url = new File("src/main/java/frontend/SplashScreen.fxml").toURI().toURL();
        primaryStage.setTitle("Welcome to Hi-5 Scheduling");
        Parent root = FXMLLoader.load(url);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static Graph getGraph(){
        return _graph;
    }

}
