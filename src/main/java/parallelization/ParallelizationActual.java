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
//    private Graph _graph;
//    private int _processors;
//    private int _cores;
//    private PSPriorityQueueChild[] _childQueues;
//    private ArrayList<PartialSolution> _solutions;
//
//
//    public ParallelizationActual(PSPriorityQueue priorityQueue, int processors, Graph graph, int cores) {
//        _parentQueue = priorityQueue;
//        _processors = processors;
//        _graph = graph;
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
//        System.out.println("************Child queues************");
//        printChildQueues();
//        System.out.println("************Child Queues************");
//    }
//
//
//    TASK private void threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {
//        System.out.println("THREAD DOING SHIT");
//        PartialSolution ps = null;
//        while (childQueue.hasNext()){
//        ps = childQueue.getCurrentPartialSolution();
//        psManager.generateChildren(ps, childQueue);
//        }
//        ps = childQueue.getCurrentPartialSolution();
//        System.out.println("8====D adding\n" + ps);
//        _solutions.add(ps);
//        }
//
//public PartialSolution findOptimal() throws ExecutionException, InterruptedException {
//        PartialSolution[] ps = new PartialSolution[_cores];
//        TaskIDGroup g = new TaskIDGroup(_cores);
//        for (int i = 0; i < _cores; i++) {
//        PSManager psManager = new PSManager(_processors, _graph);
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
//public void printSolutions() {
//
//        System.out.println("**************ZIHAO'S OUPUT********************");
//
//        for (PartialSolution solution: _solutions) {
//        System.out.println(solution.toString());
//        }
//
//        System.out.println("**************ZIHAO'S OUPUT********************");
//        }
//
//public void printChildQueues() {
//        for (int i = 0; i < _childQueues.length; i++) {
//
//        System.out.println("queue " + i);
//
//        PSPriorityQueueChild queue = _childQueues[i];
//        queue.printQueue();
//        }
//        }
//
//        }
