package algorithm;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;

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
    public ArrayList<String> _nodes; //trialing string to show nodes in solution;
    public int[] _startingNodes;
    public int[] _startingNodeIndices;
    public int _zeroStarts;
    public int _priority;

    public HashMap<Integer, ProcessorSlot> _slotMap;

    public PartialSolution(int numberOfProcessors) {
        _processors = new ArrayList[numberOfProcessors];
        for (int i = 0; i < numberOfProcessors; i++) {
            _processors[i] = new ArrayList<>();
        }
        _nodes = new ArrayList<>();
        _priority = 0;
        _latestSlots = new ProcessorSlot[numberOfProcessors];
        _startingNodes = new int[numberOfProcessors];
        _startingNodeIndices = new int[numberOfProcessors];
        _zeroStarts = numberOfProcessors;
        _slotMap = new HashMap<>();

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
        _priority = ps._priority;
        _latestSlots = new ProcessorSlot[ps._latestSlots.length];
        for (int i = 0; i < _latestSlots.length; i++) {
            _latestSlots[i] = ps._latestSlots[i];
        }
        _processors = new ArrayList[ps._processors.length];
        for (int i = 0; i < _processors.length; i++) {
            _processors[i] = new ArrayList<>(ps._processors[i]);
        }
        _nodes = new ArrayList<>(ps._nodes);
        if (ps._zeroStarts == 0) { // starts are all full, reuse
            _zeroStarts = 0;
            _startingNodeIndices = ps._startingNodeIndices;
            _startingNodes = ps._startingNodes;
        } else {
            _zeroStarts = ps._zeroStarts;
            _startingNodeIndices = ps._startingNodeIndices.clone();
            _startingNodes = ps._startingNodes.clone();
        }
        _slotMap = new HashMap<>(ps._slotMap);
    }

    public int compareTo(PartialSolution o) {
        double costDiff = _cost - o._cost;
        if (costDiff == 0) {
            int nodeDiff = o._nodes.size() - _nodes.size();
            if (nodeDiff == 0) {
                return _priority - o._priority;
            } else {
                return nodeDiff;
            }
        } else {
            return costDiff > 0 ? 1 : -1;
        }
    }

    public ArrayList<ProcessorSlot>[] getProcessors() {
        return _processors;
    }

    @Override // TODO: remove when working
    /**
     * glorified logging
     */
    public String toString() {
        String s = "\n===========================\n";
        for (int i = 0; i < _processors.length; i++) {
            s += "\nPROCESSOR " + (i+1) + "\n";
            for (ProcessorSlot slot : _processors[i]) {
                s += "[Time: " + slot.getStart() + " -> " + slot.getFinish() + "] Task: " + slot.getNode().getName() +  "\n";
            }
        }
        s+= "\nCost: " + _cost;
        s += "\n\n===========================\n";
        return s;
    }
}


