package frontend;

import graph.Node;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        File testGraph = new File ("src/test/resources/exampleSmall.dot");
        _graph = parseDotFile(testGraph);
        List<Node> listOfNodes = _graph.getNodes();

        URL url = new File("src/main/java/frontend/SplashScreen.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Welcome to Hi-5 Scheduling");
        primaryStage.setScene(new Scene(root));
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
