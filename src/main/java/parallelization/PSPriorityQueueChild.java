package parallelization;

import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import graph.Graph;

import java.util.PriorityQueue;

/**
 * This is a child of the PSPriorityQueue class.
 * It is used only during parallelization when the queues are split for preparation.
 * Created by zihaoyang on 19/08/17.
 */
public class PSPriorityQueueChild extends PSPriorityQueue {

    int _id;

    public PSPriorityQueueChild(Graph graph, int processors, int id, PriorityQueue queue) {
        super(graph, processors);
        _id = id;
        _queue = queue;
    }


    // Print the child queue
    public void printQueue() {
        for (PartialSolution ps: _queue) {
            System.out.println(ps.toString());
        }
    }





}
