package frontend;

import algorithm.PartialSolution;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;

/**
 * Created by nateeo on 25/08/17.
 */
public class Listener {
    AnchorPane _schedulePane;
    AnchorPane _statsPane;
    SplashScreen _ss;

    public Listener(AnchorPane schedulePane, AnchorPane statsPane) {
        _schedulePane = schedulePane;
        _statsPane = statsPane;
    }

    public Listener(SplashScreen ss) {
        _ss = ss;
    }

    public void notify(String message, PartialSolution ps) {
        System.out.println(_ss.schedulerPane);
        System.out.println(_ss);
            if (_ss.schedulerPane != null) {
                ScheduleGraphGenerator sgg = new ScheduleGraphGenerator(ps);
                Platform.runLater(() -> _ss.schedulerPane.getChildren().set(0, sgg.generateGraph()));
            }

    }

    public void update(int[] nodeCounts, int memory, int cost, int currentFinishTime, int statesExplored){
        if(nodeCounts != null){
            _ss._gd.updateHeatMap(nodeCounts);
            // SAM LI UPDATE YOUR STUFF HERE
        }
    }
}