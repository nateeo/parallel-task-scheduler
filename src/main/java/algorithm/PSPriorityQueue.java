package algorithm;

import graph.Graph;
import graph.Node;
import parallelization.PSPriorityQueueChild;

import java.util.PriorityQueue;

/**
 * A priority queue of partial solutions, priority is calculated from their underestimates of cost
 * It wraps a Java PriorityQueue, allowing a check for a complete solution before popping off the highest
 * priority PartialSolution while initialising it with an estimated length.
 */
public class PSPriorityQueue {
    protected PriorityQueue<PartialSolution> _queue;
    protected Graph _graph;
    private int _totalNodes;
    private int _processors;
    protected PartialSolution _currentPartialSolution;
    private PSManager _psManager;

    public PSPriorityQueue(Graph graph, int processors) {
        _graph = graph;
        _totalNodes = _graph.getNodes().size();
        _processors = processors;
        // TODO: capacity heuristic
        _queue = new PriorityQueue<PartialSolution>(graph.getNodes().size() * processors * 5);
        _psManager = new PSManager(processors, graph);
    }

    /**
     * Initialise the priority queue with the starting states
     */
    public void initialise() {
        for (Node node : _graph.getStart()) {
            PartialSolution ps = new PartialSolution(_processors);
            ProcessorSlot slot = new ProcessorSlot(node, 0, 0);
            _psManager.addSlot(ps, slot);
            _psManager.calculateUnderestimate(ps);
            _queue.add(ps);
        }
    }

    /**
     * Loads the next PartialSolution and returns true if it is complete
     * @return
     */
    public boolean hasNext() {
        if (!_queue.isEmpty()) {
            _currentPartialSolution = _queue.poll();
//            System.out.println("current partial solution has " + _currentPartialSolution._nodes.size());
//            System.out.println("totalNodes: " + _totalNodes);
//            System.out.println("HAS NEXT??? " + (_currentPartialSolution._nodes.size() != _totalNodes));
            return _currentPartialSolution._nodes.size() != _totalNodes;
        } else {
            System.out.println("Size is 0????");
            return false;
        }

    }

    /**
     * Returns the current PartialSolution (loaded by the hasNext method)
     * @return
     */
    public PartialSolution getCurrentPartialSolution() {
        return _currentPartialSolution;
    }

    public boolean contains(PartialSolution ps) {
       return _queue.contains(ps);
    }

    public void add(PartialSolution e) {
        _queue.add(e);
    }

    public int size() {
        return _queue.size();
    }

    public PSPriorityQueueChild[] splitQueue(int cores){
        PriorityQueue<PartialSolution>[] queues = new PriorityQueue[4];
        for (int i = 0; i < cores; i++) {
            queues[i] = new PriorityQueue<>();

        }

        int originalQueueSize = _queue.size();
        int counter = 0;
        for (int i = 0; i < originalQueueSize; i++) {
            queues[counter].add(_queue.poll());

            counter++;
            if (counter == cores){
                counter = 0;
            }
        }

//        for (int i = 0; i < cores; i++){
//            System.out.println("THIS QUEUE...:");
//            for (PartialSolution ps : queues[i]){
//                System.out.println(ps.toString());
//            }
//        }

        PSPriorityQueueChild[] childQueues = new PSPriorityQueueChild[cores];
        for (int i = 0; i < cores; i++) {
            childQueues[i] = new PSPriorityQueueChild(_graph, _processors, i, queues[i]);
        }

        return childQueues;
    }
}
