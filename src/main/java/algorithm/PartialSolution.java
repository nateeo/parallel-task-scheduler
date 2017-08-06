package algorithm;

import graph.Node;

import java.util.ArrayList;

/**
 *  This class creates temporary representations of the schedules being used to find the optimal schedule.
 *  Author: Sam Li, Edison Rho, Nathan Hur
 */

public class PartialSolution implements Comparable<PartialSolution> {

    protected int _idleTime; // idle time heuristic (Total idle time + total node work) / processors TODO: test
    protected int _cost; // overall cost heuristic
    protected int _currentFinishTime; // the finish time of the lowest node in the schedule
    protected int _nodeCount;
    protected ArrayList<ProcessorSlot>[] _processors;

    public PartialSolution(int numberOfProcessors) {
        _processors = new ArrayList[numberOfProcessors];
        for (int i = 0; i < numberOfProcessors; i++) {
            _processors[i] = new ArrayList<ProcessorSlot>();
        }
        _idleTime = 0;
        _cost = 0;
        _nodeCount = 0;
        _currentFinishTime = 0;
    }

    /**
     * check if a node is present within the schedule of the partial schedule.
     * @param node
     * @return
     */
    protected boolean contains(Node node){
        for (ArrayList<ProcessorSlot> processor : _processors){
            if (processor.contains(node)){
                return true;
            }
        }
        return false;
    }

    public int compareTo(PartialSolution o) {
        return this._cost - o._cost;
    }
}
