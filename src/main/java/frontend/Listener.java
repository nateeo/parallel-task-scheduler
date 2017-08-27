package frontend;

import algorithm.PartialSolution;
import javafx.application.Platform;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Listener to receive data updates to display on the visualisation
 * Created by nateeo on 25/08/17.
 */
public class Listener {
    SplashScreen _ss;
    AtomicInteger[] _nodeVisitCounts;
    // threads only overwrite their own indices
    int[] _memory;
    int[] _statesExplored;

    public Listener(SplashScreen ss) {
        _ss = ss;
        int size = ss._graph.getNodes().size();
        _nodeVisitCounts = new AtomicInteger[size];
        _memory = new int[size];
        _statesExplored = new int [size];
        for (int i = 0; i < size; i++) {
            _nodeVisitCounts[i] = new AtomicInteger();
        }
    }

    public void update(boolean isFinished, PartialSolution ps, int[] nodeCounts, int memory, int cost, int currentFinishTime, int statesExplored, double loaded){
        this.sendUpdate(isFinished, 0, ps, nodeCounts, memory, cost, currentFinishTime, statesExplored, loaded);
    }

    public void updateThread(boolean isFinished, PartialSolution ps, int id, int[] nodeVisitCounts, int memory, int cost, int currentFinishTime, int statesExplored, double loaded) {
        int[] currentNodeVisitCount = new int[_nodeVisitCounts.length];
        for (int j = 0; j < nodeVisitCounts.length; j++) {
            currentNodeVisitCount[j] = _nodeVisitCounts[j].addAndGet(nodeVisitCounts[j]);
        }
        _memory[id] = memory;
        _statesExplored[id] = statesExplored;

        // display totals for memory and states explored
        int totalMemory = 0;
        int totalStatesExplored = 0;
        for (int i = 0; i < _memory.length; i++) {
            totalMemory += _memory[i];
            totalStatesExplored += _statesExplored[i];
        }
        this.sendUpdate(isFinished, id, ps, currentNodeVisitCount, totalMemory, cost, currentFinishTime, totalStatesExplored, loaded);
    }

    private void sendUpdate(boolean isFinished, int id, PartialSolution ps, int[] nodeCounts, int memory, int cost, int currentFinishTime, int statesExplored, double loaded){
        new Thread(() -> {
            System.out.println("updating");
            if (ps == null) return;
            double maxVisitedValue = 0;
            double[] saturationValues = new double[nodeCounts.length];
            for (int value : nodeCounts) {
                if (value > maxVisitedValue) maxVisitedValue = value;
            }
            for (int i = 0; i < nodeCounts.length; i++) {
                double val = (double)nodeCounts[i] / maxVisitedValue;
                saturationValues[i] = val > 1 ? 1 : val;
            }
            final ScheduleGraphGenerator sgg = new ScheduleGraphGenerator(ps);
            if(nodeCounts != null && _ss != null && _ss._gd != null){
                Platform.runLater(() -> {
                    _ss._gd.updateHeatMap(saturationValues);
                    _ss._sg.updateStats(isFinished, loaded,id,currentFinishTime,cost,statesExplored,memory);
                    if (_ss.schedulerPane != null) _ss.schedulerPane.setContent(sgg.generateGraph());
                });
            } else {
                System.out.println(":Ge");
            }
        }).start();

    }
}
