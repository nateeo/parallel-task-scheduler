package algorithm;

import graph.Node;

/**
 * This class is a wrapper for the nodes, it adds the cost and time that each node is running.
 * Author: Sam Li, Edison Rho and Nathan Hur.
 */

public class ProcessorSlot {
    protected Node _node;
    protected int _start;
    protected int _finish;
    protected int _processor;

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


}
