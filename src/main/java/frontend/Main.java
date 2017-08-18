package frontend;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import graph.Graph;

import static dotParser.Parser.parseDotFile;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private File testGraph = new File ("../../../test/resources/exampleSmall.dot");
    private Graph graph = parseDotFile(testGraph);

    @Override
    public void start(Stage primaryStage) {

    }

    public void addGraph() {

    }
}
