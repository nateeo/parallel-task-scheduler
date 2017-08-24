package algorithm;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 *  This class creates temporary representations of the schedules being used to find the optimal schedule.
 *  Author: Sam Li, Edison Rho, Nathan Hur
 */

public class PartialSolution implements Comparable<PartialSolution> {

    public int _idleTime; // total idle time (between slots)
    public int _cost; // overall cost heuristic
    public int _bottomLevelWork;
    public int _currentFinishTime; // the finish time of the lowest node in the schedule
    public ProcessorSlot _latestSlot;
    public ProcessorSlot[] _latestSlots;

    public ArrayList<ProcessorSlot>[] _processors;
    public ArrayList<String> _nodes = new ArrayList<>(); //trialing string to show nodes in solution;
    public TreeMap<Integer, Integer> _id; // node id -> array int

    public PartialSolution(int numberOfProcessors) {
        _processors = new ArrayList[numberOfProcessors];
        _id = new TreeMap<>();
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
        _bottomLevelWork = ps._bottomLevelWork;
        _currentFinishTime = ps._currentFinishTime;
        _latestSlot = ps._latestSlot;
        _latestSlots = new ProcessorSlot[ps._latestSlots.length];
        for (int i = 0; i < _latestSlots.length; i++) {
            _latestSlots[i] = ps._latestSlots[i];
        }
        _processors = new ArrayList[ps._processors.length];
        for (int i = 0; i < _processors.length; i++) {
            _processors[i] = new ArrayList<>(ps._processors[i]);
        }
        _nodes = (ArrayList)ps._nodes.clone();
        if (ps._id.size() == ps._processors.length) {
            _id = ps._id;
        } else {
            _id = (TreeMap)ps._id.clone();
        }
    }

    public int compareTo(PartialSolution o) {
        if (_cost == o._cost) {
            return o._nodes.size() - _nodes.size(); // if equal costs for PartialSolutions, compare based on number of nodes
        }
        return _cost - o._cost;
    }

    public ArrayList<ProcessorSlot>[] getProcessors() {
        return _processors;
    }

    @Override // TODO: remove when working
    /**
     * glorified logging
     */
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
