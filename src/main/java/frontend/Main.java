package frontend;

import algorithm.PSManagerGroup;
import graph.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.Scheduler;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    static Graph _graph;


    /**
     * loads and displays the splash screen frontend.SplashScreen.fxml
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        _graph = Scheduler._graph;

        URL url = new File("src/main/java/frontend/SplashScreen.fxml").toURI().toURL();
        primaryStage.setTitle("Welcome to Hi-5 Scheduling");
        FXMLLoader loader = new FXMLLoader();
        SplashScreen controller = new SplashScreen();
        loader.setController(controller);

        Parent root = loader.load(url);
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();


    }

    /**
     * informs the scheduler to stop
     */
    @Override
    public void stop(){
        // Tell scheduler that the updater can stop
        Scheduler._stopTimer = true;
        System.exit(0);
    }


    /**
     * returns the input graph
     * @return the input graph
     */
    public static Graph getGraph(){
        return _graph;
    }

}
