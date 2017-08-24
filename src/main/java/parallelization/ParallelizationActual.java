//package parallelization;
//
//import algorithm.Cache;
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
//    private Graph _graph;
//    private int _processors;
//    private int _cores;
//    private Cache _cache;
//    private PSPriorityQueueChild[] _childQueues;
//    private ArrayList<PartialSolution> _solutions;
//
//
//    public ParallelizationActual(PSPriorityQueue priorityQueue, int processors, Graph graph, int cores, Cache cache) {
//        _parentQueue = priorityQueue;
//        _processors = processors;
//        _graph = graph;
//        _cores = cores;
//        _cache = cache;
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
//
//    TASK private void threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {
//        PartialSolution ps = null;
//        while (childQueue.hasNext()){
//        ps = childQueue.getCurrentPartialSolution();
//        psManager.generateChildren(ps, childQueue);
//        }
//        ps = childQueue.getCurrentPartialSolution();
//        _solutions.add(ps);
//        }
//
//public PartialSolution findOptimal() throws ExecutionException, InterruptedException {
//        PartialSolution[] ps = new PartialSolution[_cores];
//        TaskIDGroup g = new TaskIDGroup(_cores);
//        for (int i = 0; i < _cores; i++) {
//        PSManager psManager = new PSManager(_processors, _graph, _cache);
//        TaskID id = threadQueue(_childQueues[i], psManager);
//        g.add(id);
//        }
//        g.waitTillFinished();
//        for (PartialSolution p : _solutions){
//        }
//        PartialSolution solution = null;
//        int finalTime = -1;
//        for (int i = 0; i < _solutions.size(); i++) {
//        int psFinishTime = _solutions.get(i)._latestSlot.getFinish();
//        if (finalTime == -1 || psFinishTime < finalTime) {
//        solution = _solutions.get(i);
//        finalTime = psFinishTime;
//        }
//        }
//        return solution;
//        }
//
//        }
