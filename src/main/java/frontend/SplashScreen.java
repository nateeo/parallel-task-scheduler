package frontend;

import algorithm.PSManagerGroup;
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
    protected ScrollPane schedulerPane;

    @FXML
    protected Label timer;

    @FXML
    protected Label currentThread;

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

        //draw graph
        _graph = Main.getGraph();
        _gd = new GraphDrawer(_graph, graphPane, circleSize);
        _gd.drawGraph();
        _sg = new StatsGenerator(progressBar, timer, currentThread ,currentFinishTime, underestimate, statesExplored, memory);
        listener = new Listener(this);
        Scheduler._listener = listener;
        PSManagerGroup._listener = listener;
        //generate stats




        try {

            ScheduleGraphGenerator sgm = new ScheduleGraphGenerator();

            currentSGM = sgm.initialise();
            schedulerPane.setContent(currentSGM);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


