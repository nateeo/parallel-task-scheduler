package algorithm;

import graph.Graph;
import graph.Node;
import logger.Logger;

import java.util.PriorityQueue;

/**
 * A priority queue of partial solutions, priority is calculated from their underestimates of cost
 * It wraps a Java PriorityQueue, allowing a check for a complete solution before popping off the highest
 * priority PartialSolution while initialising it with an estimated length.
 */
public class PSPriorityQueue {
    private PriorityQueue<PartialSolution> _queue;
    private Graph _graph;
    private int _totalNodes;
    private int _processors;
    private PartialSolution _currentPartialSolution;
    private PSManager _psManager;

    public PSPriorityQueue(Graph graph, int processors) {
        _graph = graph;
        _totalNodes = _graph.getNodes().size();
        _processors = processors;
        // TODO: capacity heuristic
        _queue = new PriorityQueue<PartialSolution>(graph.getNodes().size() * processors * 5);
        _psManager = new PSManager(processors, graph);
        initialise();
    }

    /**
     * Initialise the priority queue with the starting states
     */
    public void initialise() {
        for (Node node : _graph.getStart()) {
            for (int i = 0; i < _processors; i++) {
                PartialSolution ps = new PartialSolution(_processors);
                ProcessorSlot slot = new ProcessorSlot(node, 0, i);
                _psManager.addSlot(ps, slot);
                _psManager.calculateUnderestimate(ps);
                _queue.add(ps);
            }
        }
    }

    /**
     * Loads the next PartialSolution and returns true if it is complete
     * @return
     */
    public boolean hasNext() {
        _currentPartialSolution = _queue.poll();
        Logger.info("current is");
        System.out.println(_currentPartialSolution);
        if (_queue.isEmpty()) {
         Logger.info("QUEUE FINISHED");
         return false;
        }
        return _currentPartialSolution._nodeCount != _totalNodes;
    }

    /**
     * Returns the current PartialSolution (loaded by the hasNext method)
     * @return
     */
    public PartialSolution getCurrentPartialSolution() {
        return _currentPartialSolution;
    }

    public void add(PartialSolution e) {
        _queue.add(e);
    }
}
