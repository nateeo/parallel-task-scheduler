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


    /**
     * creates the listener object based on the frontend.SplashScreen object
     * @param ss
     */
    public Listener(SplashScreen ss) {
        _ss = ss;
        int size = ss._graph.getNodes().size();
        // atomicintegers to make _nodeVisitCounts thread safe
        // as it will be updated by all threads
        _nodeVisitCounts = new AtomicInteger[size];
        _memory = new int[size];
        _statesExplored = new int [size];

        // initialise the _nodeVisitCounts array
        for (int i = 0; i < size; i++) {
            _nodeVisitCounts[i] = new AtomicInteger();
        }
    }

    /**
     * hook method to update all the fields required by the front end
     * @param isFinished
     * @param ps
     * @param nodeCounts
     * @param memory
     * @param cost
     * @param currentFinishTime
     * @param statesExplored
     * @param loaded
     */
    public void update(boolean isFinished, PartialSolution ps, int[] nodeCounts, int memory, int cost, int currentFinishTime, int statesExplored, double loaded){
        this.sendUpdate(isFinished, 0, ps, nodeCounts, memory, cost, currentFinishTime, statesExplored, loaded);
    }

    /**
     * when running in a parallel system this method updates the stats for one thread
     * @param isFinished
     * @param ps
     * @param id
     * @param nodeVisitCounts
     * @param memory
     * @param cost
     * @param currentFinishTime
     * @param statesExplored
     * @param loaded
     */
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

    /**
     * fires events to the frontend to update the stats
     * @param isFinished
     * @param id
     * @param ps
     * @param nodeCounts
     * @param memory
     * @param cost
     * @param currentFinishTime
     * @param statesExplored
     * @param loaded
     */
    private void sendUpdate(boolean isFinished, int id, PartialSolution ps, int[] nodeCounts, int memory, int cost, int currentFinishTime, int statesExplored, double loaded){
        new Thread(() -> {
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
            }
        }).start();

    }
}
