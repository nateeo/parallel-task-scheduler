package parallelization;

import algorithm.PSManager;
import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import graph.Graph;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

/**
 * Created by zihaoyang on 19/08/17.
 * @author sueyeonlee
 * @author zihaoyang
 */
public class ParallelizationActual {

    private PSPriorityQueue _parentQueue;
    private  PSManager _psManager;
    private Graph _graph;
    private int _processors;
    private int _cores;
    private PSPriorityQueueChild[] _childQueues;
    private PartialSolution _solution;
    private double _finalTime;


    public ParallelizationActual(PSPriorityQueue priorityQueue, PSManager psManager, int cores) {
        _parentQueue = priorityQueue;
        _psManager = psManager;
        _cores = cores;
        _childQueues = new PSPriorityQueueChild[cores];
        _solution = null;
        _finalTime = Double.POSITIVE_INFINITY;

        initialiseQueues();

    }

    public void initialiseQueues() {
        PSPriorityQueueChild[] queues = _parentQueue.splitQueue(_cores);

        for (int i = 0; i < _cores; i++) {
            _childQueues[i] = queues[i];
        }
    }

//    TASK private void threadQueue(PSPriorityQueueChild childQueue) {
//          System.out.println("THREAD DOING SHIT");
//        PartialSolution ps = null;
//        while (childQueue.hasNext()){
//            ps = childQueue.getCurrentPartialSolution();
//            _psManager.generateChildren(ps, childQueue);
//        }
//        ps = childQueue.getCurrentPartialSolution();
//        if (ps._currentFinishTime < _finalTime) {
//            _solution = ps;
//            _finalTime = ps.getCurrentFinshTime();
//        }
//    }

    public PartialSolution findOptimal() throws ExecutionException, InterruptedException {
        PartialSolution[] ps = new PartialSolution[_cores];
//        TaskIDGroup g = new TaskIDGroup(_cores);
        for (int i = 0; i < _cores; i++) {
            System.out.println("~~~~~ THread: " + i);
//            TaskID id = threadQueue(_childQueues[i]);
//            g.add(id);
            System.out.println("~~~~~ THread finished: " + i);
        }
//        g.waitTillFinished();
        return _solution;
    }
}