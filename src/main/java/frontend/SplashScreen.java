package frontend;

import algorithm.PSManager;
import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import graph.Edge;
import graph.Graph;
import graph.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
    GraphDrawer _gd;

    @FXML
    private AnchorPane graphPane;
    private double circleSize = 40;

    @FXML
    private AnchorPane schedulerPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        _graph = Main.getGraph();
        _gd = new GraphDrawer(_graph, graphPane, circleSize);
        _gd.drawGraph();

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

        ScheduleGraphGenerator sgm = new ScheduleGraphGenerator(ps);
        try {
            schedulerPane.getChildren().add(sgm.generateGraph(ps));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeScene(String filePathToXML, ActionEvent actionEvent) throws Exception{
        URL url = new File(filePathToXML).toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene nextScene = new Scene(root);
        Stage loadingPage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        loadingPage.setScene(nextScene);
    }

    public void changeColor(ActionEvent event) {
        _gd.handleColorChange(event);
    }

    public void testChangeColor(ActionEvent event) {
        int[] values = {80,50,32,31,21,17,13,8,1,1,1};
        _gd.updateHeatMap(values);
    }
}


