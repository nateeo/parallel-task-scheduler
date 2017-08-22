package parallelization;

import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import graph.Graph;

import java.util.PriorityQueue;

/**
 * Created by zihaoyang on 19/08/17.
 */
public class PSPriorityQueueChild extends PSPriorityQueue {

    int _id;

    public PSPriorityQueueChild(Graph graph, int processors, int id, PriorityQueue queue) {
        super(graph, processors);
        _id = id;
        _queue = queue;
    }





}
