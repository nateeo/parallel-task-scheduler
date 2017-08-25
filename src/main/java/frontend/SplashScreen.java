package frontend;

import graph.Graph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import scheduler.Scheduler;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The splash Screen is meant to provide visual feedback to the user while it waits for the Task Schedular algorithm to complete
 * running.
 *
 * Author: Samule Li, Edison Rho
 */

public class SplashScreen implements Initializable {

    Graph _graph;
    GraphDrawer _gd;
    ScrollPane currentSGM;
    Listener listener;
    StatsGenerator _sg;

    @FXML
    private AnchorPane graphPane;
    private double circleSize = 40;

    @FXML
    protected AnchorPane statsPane;

    @FXML
    protected AnchorPane schedulerPane;

    @FXML
    protected Label timer;

    @FXML
    protected Label currentFinishTime;

    @FXML
    protected Label underestimate;

    @FXML
    protected Label statesExplored;

    @FXML
    protected Label memory;

    @FXML
    private ProgressIndicator progressBar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("GEasg");
        listener = new Listener(this);
        Scheduler._listener = listener;
        System.out.println("assign " + Scheduler._listener);
        System.out.println(statsPane);
        System.out.println(schedulerPane);

        //draw graph
        _graph = Main.getGraph();
        _gd = new GraphDrawer(_graph, graphPane, circleSize);
        _gd.drawGraph();
        _sg = new StatsGenerator(progressBar, timer, currentFinishTime, underestimate, statesExplored, memory);
        //generate stats




        try {

            ScheduleGraphGenerator sgm = new ScheduleGraphGenerator();

            currentSGM = sgm.initialise();
            schedulerPane.getChildren().add(currentSGM);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testChangeColor(ActionEvent event) {
        int[] values = {80,50,32,31,21,17,13,8,1,1,1};
        _gd.updateHeatMap(values);
    }

    public void testUpdateStats(ActionEvent event) {
        //_sg.updateStats(0.6,12,12,12);
    }
}


