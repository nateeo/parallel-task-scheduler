//package parallelization;
//
//import algorithm.PSManager;
//import algorithm.PSPriorityQueue;
//import algorithm.PartialSolution;
//import graph.Graph;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.PriorityQueue;
//import java.util.concurrent.ExecutionException;
//
///**
// * Created by zihaoyang on 19/08/17.
// * @author sueyeonlee
// * @author zihaoyang
// */
//public class ParallelizationActual {
//
//    private PSPriorityQueue _parentQueue;
//    private  PSManager _psManager;
//    private Graph _graph;
//    private int _processors;
//    private int _cores;
//    private PSPriorityQueueChild[] _childQueues;
//    private ArrayList<PartialSolution> _solutions;
//
//
//    public ParallelizationActual(PSPriorityQueue priorityQueue, PSManager psManager, int cores) {
//        _parentQueue = priorityQueue;
//        _psManager = psManager;
//        _cores = cores;
//        _childQueues = new PSPriorityQueueChild[cores];
//        _solutions = new ArrayList<PartialSolution>();
//
//        initialiseQueues();
//
//    }
//
//    public void initialiseQueues() {
//        PSPriorityQueueChild[] queues = _parentQueue.splitQueue(_cores);
//
//        for (int i = 0; i < _cores; i++) {
//            _childQueues[i] = queues[i];
//        }
//    }
//
//    TASK private void threadQueue(PSPriorityQueueChild childQueue) {
//          System.out.println("THREAD DOING SHIT");
//        PartialSolution ps = null;
//        while (childQueue.hasNext()){
//            ps = childQueue.getCurrentPartialSolution();
//            _psManager.generateChildren(ps, childQueue);
//        }
//        System.out.println("child queue size " + childQueue.size());
//        ps = childQueue.getCurrentPartialSolution();
//        System.out.println("8====D adding\n" + ps);
//        _solutions.add(ps);
//    }
//
//    public PartialSolution findOptimal() throws ExecutionException, InterruptedException {
//        PartialSolution[] ps = new PartialSolution[_cores];
//        TaskIDGroup g = new TaskIDGroup(_cores);
//        for (int i = 0; i < _cores; i++) {
//            System.out.println("~~~~~ THread: " + i);
//            TaskID id = threadQueue(_childQueues[i]);
//            System.out.println("THREAD ID: " + id);
//            g.add(id);
//            System.out.println("~~~~~ THread finished: " + i);
//        }
//        g.waitTillFinished();
//        PartialSolution solution = null;
//        int finalTime = -1;
//        for (int i = 0; i < _solutions.size(); i++) {
//            int psFinishTime = _solutions.get(i)._latestSlot.getFinish();
//            if (finalTime == -1 || psFinishTime < finalTime) {
//                solution = _solutions.get(i);
//                finalTime = psFinishTime;
//            }
//        }
//        return solution;
//    }
//}
