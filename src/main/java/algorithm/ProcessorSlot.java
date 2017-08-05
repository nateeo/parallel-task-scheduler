package algorithm;

import graph.Node;

/**
 * This class is a wrapper for the nodes, it adds the cost and time that each node is running.
 * Author: Sam Li, Edison Rho and Nathan Hur.
 */

public class ProcessorSlot {
    private Node _node;
    private int _time;

    public ProcessorSlot (Node node, int time) {
        _node = node;
        _time = time;
    }

    public Node getNode() {
        return _node;
    }

    public int getTime() {
        return _time;
    }
}
