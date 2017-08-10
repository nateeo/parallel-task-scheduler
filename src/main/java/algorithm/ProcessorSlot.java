package algorithm;

import graph.Node;

/**
 * This class is a wrapper for the nodes, it adds the cost and time that each node is running.
 * Author: Sam Li, Edison Rho and Nathan Hur.
 */

public class ProcessorSlot implements Comparable<ProcessorSlot> {
    private Node _node;
    private int _start;
    private int _finish;
    private int _processor;

    public ProcessorSlot (Node node, int start, int finish, int processor) {
        _node = node;
        _start = start;
        _finish = finish;
        _processor = processor;
    }

    public ProcessorSlot (Node node, int start, int processor) {
        this(node, start, start + node.getWeight(), processor);
    }

    public Node getNode() {
        return _node;
    }

    public int getStart() {
        return _start;
    }

    public int getFinish() {
        return _finish;
    }

    public int getProcessor() {
        return _processor;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof ProcessorSlot) {
            ProcessorSlot other = (ProcessorSlot)o;
            if (_start == other._start) {
                return _node.getName().equals(other._node.getName());
            }
        }
        return false;
    }

    public boolean equals(ProcessorSlot other) {
        if (_start == other._start) {
            return _node.getName().equals(other._node.getName());
        }
        return false;
    }

    @Override
    public int compareTo(ProcessorSlot o) {
        return (_start + "x" + _node.getName()).compareTo(o._start + "x" + o._node.getName());
    }
}
