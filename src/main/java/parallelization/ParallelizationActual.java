//package parallelization;
//
//import algorithm.PSManager;
//import algorithm.PSPriorityQueue;
//import algorithm.Cache;
//import algorithm.PartialSolution;
//import graph.Graph;
//import pt.runtime.TaskID;
//import pt.runtime.TaskIDGroup;
//
//import java.util.ArrayList;
//import java.util.concurrent.ExecutionException;
//
///**
// * Created by zihaoyang on 19/08/17.
// * @author sueyeonlee
// * @author zihaoyang
// */
//public class Parallelization {
//
//    private PSPriorityQueue _parentQueue;
//    private Cache _cache;
//    private Graph _graph;
//    private int _processors;
//    private int _cores;
//    private PSPriorityQueueChild[] _childQueues;
//    private ArrayList<PartialSolution> _solutions;
//    private PartialSolution _preOptimalSolution;
//    private int _fastestSolution;
//
//
//
//    public Parallelization (PSPriorityQueue priorityQueue, int processors, Graph graph, int cores, Cache cache) {
//        _parentQueue = priorityQueue;
//        _processors = processors;
//        _graph = graph;
//        _cores = cores;
//        _childQueues = new PSPriorityQueueChild[cores];
//        _solutions = new ArrayList<PartialSolution>();
//        _cache = cache;
//        _preOptimalSolution = null;
//
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
//    TASK private PartialSolution threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {
//        PartialSolution ps = null;
//        while (childQueue.hasNext()){
//            ps = childQueue.getCurrentPartialSolution();
//            if (_preOptimalSolution == null || ps._latestSlot.getFinish() < _fastestSolution){
//                psManager.generateChildren(ps, childQueue);
//            }
//            System.out.println(childQueue.size());
//        }
//        ps = childQueue.getCurrentPartialSolution();
//        System.out.println("8====D adding\n" + ps);
//        return ps;
//    }
//
//    public PartialSolution findOptimal() throws ExecutionException, InterruptedException {
//        PartialSolution[] ps = new PartialSolution[_cores];
//        TaskIDGroup g = new TaskIDGroup(_cores);
//        for (int i = 0; i < _cores; i++) {
//            PSManager psManager = new PSManager(_processors, _graph, _cache);
//            TaskID<PartialSolution> id = threadQueue(_childQueues[i], psManager);
//            PartialSolution partialSolution = id.getReturnResult();
//            if (_preOptimalSolution == null || partialSolution._latestSlot.getFinish() < _preOptimalSolution._latestSlot.getFinish()) {
//                _preOptimalSolution = partialSolution;
//                _fastestSolution = partialSolution._latestSlot.getFinish();
//                System.out.println("NEW BEST!");
//            }
//            g.add(id);
//
//        }
//        g.waitTillFinished();
//        return _preOptimalSolution;
//    }
//
//    public void printSolutions() {
//
//        System.out.println("**************ZIHAO'S OUPUT********************");
//
//        for (PartialSolution solution: _solutions) {
//            System.out.println(solution.toString());
//        }
//
//        System.out.println("**************ZIHAO'S OUPUT********************");
//    }
//
//    public void printChildQueues() {
//        for (int i = 0; i < _childQueues.length; i++) {
//
//            System.out.println("queue " + i);
//
//            PSPriorityQueueChild queue = _childQueues[i];
//            queue.printQueue();
//        }
//    }
//
//}
