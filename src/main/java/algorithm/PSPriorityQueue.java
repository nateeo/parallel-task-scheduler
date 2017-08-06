package algorithm;

import graph.Graph;
import graph.Node;

import java.util.PriorityQueue;

/**
 * A priority queue of partial solutions, priority is calculated from their underestimates of cost
 * It wraps a Java PriorityQueue, allowing a check for a complete solution before popping off the highest
 * priority PartialSolution while initialising it with an estimated length.
 */
public class PSPriorityQueue {
    private PriorityQueue<PartialSolution> _queue;
    private Graph _graph;
    private int _processors;

    public PSPriorityQueue(Graph graph, int processors) {
        _graph = graph;
        _processors = processors;
        _queue = new PriorityQueue<PartialSolution>(graph.getNodes().size() * processors * 5);
    }

    /**
     * Initialise the priority queue with the starting states
     */
    public void initialise() {
        for (Node node : _graph.getStart()) {
            PartialSolution ps = new PartialSolution(_processors);
            for (int i = 0; i < _processors; i++) {

            }
        }
    }
}
