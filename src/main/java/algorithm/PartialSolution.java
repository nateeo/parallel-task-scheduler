package algorithm;

import java.util.ArrayList;

/**
 *  This class creates temporary representations of the schedules being used to find the optimal schedule.
 *  Author: Sam Li, Edison Rho, Nathan Hur
 */

public class PartialSolution implements Comparable<PartialSolution> {

    protected int _idleTime; // total idle time (between slots)
    protected int _cost; // overall cost heuristic
    protected int _currentFinishTime; // the finish time of the lowest node in the schedule
    protected int _nodeCount;
    protected ProcessorSlot _latestSlot;
    protected ProcessorSlot[] _latestSlots;
    protected ArrayList<ProcessorSlot>[] _processors;
    protected String _nodes = ""; //trialing string to show nodes in solution;

    public PartialSolution(int numberOfProcessors) {
        _processors = new ArrayList[numberOfProcessors];
        for (int i = 0; i < numberOfProcessors; i++) {
            _processors[i] = new ArrayList<>();
        }
        _latestSlots = new ProcessorSlot[numberOfProcessors];

    }

    /**
     * Copy constructor of parent
     * @param ps
     */
    public PartialSolution(PartialSolution ps) {
        _processors = ps._processors.clone();
        _idleTime = ps._idleTime;
        _cost = ps._cost;
        _nodes = ps._nodes;
        _nodeCount = ps._nodeCount;
        _currentFinishTime = ps._currentFinishTime;
        _latestSlots = ps._latestSlots.clone();
    }

    public int compareTo(PartialSolution o) {
        return this._cost - o._cost;
    }

    public ArrayList<ProcessorSlot>[] getProcessors() {
        return _processors;
    }

    @Override // TODO: remove when working
    public String toString() {
        String s = "===========================\nPARTIAL SOLUTION contains: " + _nodes + "\n";
        for (int i = 0; i < _processors.length; i++) {
            s += "PROCESSOR " + (i+1) + "\n";
            for (ProcessorSlot slot : _processors[i]) {
                s += "start: " + slot.getStart() + " finish: " + slot.getFinish() + " node: " + slot.getNode() + "\n";
            }
        }
        s += "\n===========================\n";
        return s;
    }
}
