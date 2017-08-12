package algorithm;

import java.util.ArrayList;

/**
 *  This class creates temporary representations of the schedules being used to find the optimal schedule.
 *  Author: Sam Li, Edison Rho, Nathan Hur
 */

public class PartialSolution implements Comparable<PartialSolution> {

    protected int _idleTime; // total idle time (between slots)
    public int _cost; // overall cost heuristic
    protected int _currentFinishTime; // the finish time of the lowest node in the schedule
    protected ProcessorSlot _latestSlot;
    protected ProcessorSlot[] _latestSlots;

    public ArrayList<ProcessorSlot>[] _processors;
    protected ArrayList<String> _nodes = new ArrayList<>(); //trialing string to show nodes in solution;
    protected String _id = "";

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
        _idleTime = ps._idleTime;
        _cost = ps._cost;
        _currentFinishTime = ps._currentFinishTime;
        _latestSlots = new ProcessorSlot[ps._latestSlots.length];
        for (int i = 0; i < _latestSlots.length; i++) {
            _latestSlots[i] = ps._latestSlots[i];
        }
        _processors = new ArrayList[ps._processors.length];
        for (int i = 0; i < _processors.length; i++) {
            _processors[i] = new ArrayList<>(ps._processors[i]);
        }
        _nodes = (ArrayList)ps._nodes.clone();
        _id = ps._id;
    }

    public int compareTo(PartialSolution o) {
        return this._cost - o._cost;
    }

    public ArrayList<ProcessorSlot>[] getProcessors() {
        return _processors;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PartialSolution) {
            PartialSolution other = (PartialSolution) o;
            return _id.equals(other._id);
        }
        return false;
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
        s+= "cost estimate: " + _cost;
        s += "\n===========================\n";
        return s;
    }
}
