package algorithm;

import graph.Node;

import java.util.ArrayList;
import java.util.List;

/**
 *  This class creates temporary representations of the schedules being used to find the optimal schedule.
 *  Author: Sam Li, Edison Rho, Nathan Hur
 */

public class PartialSolution {
    private int _idleTime;
    private int _cost;
    private ArrayList<ProcessorSlot>[] _processors;
    private ProcessorSlot _processorSlot;

    public PartialSolution(int numberOfProcessors) {
        _processors = new ArrayList[numberOfProcessors];
        for (int i = 0; i < numberOfProcessors; i++) {
            _processors[i] = new ArrayList<ProcessorSlot>();
        }
    }
}
