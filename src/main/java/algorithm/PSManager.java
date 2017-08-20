package algorithm;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.*;

/**
 * This static class utilises the Partial solutions to generate children partial solutions.
 * author: Sam Li, Edison Rho, Nathan Hur
 */

public class PSManager {

    private Graph _graph;
    private int _numberOfProcessors;

    //calculate all bottom level work values and cache them for the cost function
    private HashMap<String, Integer> _bottomLevelWork;
    private Cache _cache;

    //cache the constant portion of the idle time heuristic (total work / processors)
    private int _idleConstantHeuristic;

    private ArrayList<PartialSolution> _closed = new ArrayList<>();

    public PSManager(int processors, Graph graph){
        _numberOfProcessors = processors;
        _graph = graph;
        _idleConstantHeuristic = graph.totalMinimumWork() / processors;
        _bottomLevelWork = bottomLevelCalculator(graph);
        _cache = new Cache(processors);
    }

    //BFS of  children of partial solution
    //for ever node, addNode to add to partial solution then return that
    //calculate functional cost i.e. the max formula

    /**
     * Add the children of a partial solution to a given PSPriorityQueue
     * It uses all the free variables that are available from the input partial solution
     * and generates partial solutions from those free variables.
     * @param parentPS the parent partial solution to generate children from
     * @param queue the queue to add the children to
     * @return
     */
    public void generateChildren(PartialSolution parentPS, PSPriorityQueue queue) {
        List<Node> freeNodes = getFreeNodes(parentPS);
        PartialSolution partialSolution = null;
        //for every free node, create the partial solutions that can be generated
        for (Node freeNode: freeNodes) {
            //calculate latest time to put on (dependency)
            int[] earliestTimeOnProcessor = earliestTimeOnProcessors(parentPS, freeNode);
            //for every processor, create partial solution that can be generated by scheduling the
            //free variable to that processor. Calculate the new cost and add it to the priority queue.
            for (int i = 0; i < _numberOfProcessors; i++) {
                ProcessorSlot slot = new ProcessorSlot(freeNode, earliestTimeOnProcessor[i], i);
                partialSolution = new PartialSolution(parentPS);
                addSlot(partialSolution, slot);
                calculateUnderestimate(partialSolution);
                checkAndAdd(partialSolution, queue);
            }
        }
        _cache.add(parentPS);
    }

    /**
     * Calculates the bottomLevel value for all nodes in the graph, this only has to be run once in
     * the initialization. The bottom level of a source node is the sum of all the weights of each individual
     * node in the longest path originating from the source node. This function works backwards starting from
     * the leaf nodes moving up towards the root nodes.
     * @param graph the graph that contains all the nodes and edges parsed from an input .dot file
     * @return HashMap<String, Integer> of bottomLevels. The String is the name of the node and the Integer
     * is the bottom level value that is calculated.
     */
    private static HashMap<String,Integer> bottomLevelCalculator(Graph graph) {
        List<Node> allNodes = graph.getNodes();
        HashMap<String, Integer> bottomLevels = new HashMap<String, Integer>(allNodes.size());
        Queue<Node> queuedNodes = new LinkedList<Node>();
        Node predecessorNode;
        Node currentNode;
        int maxBottomLevel;
        int currentNodeBL;
        boolean allSuccessorsCalculated;

        // Looks for all leaf nodes.
        for(Node node: allNodes) {
            if(node.getOutgoing().isEmpty()) {
                queuedNodes.add(node);
            }
        }

        // Goes through the Queue of nodes and adds their bottom level to the hashmap.
        while(!queuedNodes.isEmpty()) {
            currentNode = queuedNodes.remove();
            maxBottomLevel = 0;
            if (!currentNode.getOutgoing().isEmpty()) {
                // Grabs all successor nodes and calculates the bottom level based on the max value of all it's successors.
                for (Edge successors : currentNode.getOutgoing()) {
                    currentNodeBL = bottomLevels.get(successors.getTo().getName());
                    if (currentNodeBL > maxBottomLevel) {
                        maxBottomLevel = currentNodeBL;
                    }
                }
            }

            bottomLevels.put(currentNode.getName(),maxBottomLevel + currentNode.getWeight());

            // Grabs all predecessor nodes and checks if all their successor nodes have been calculated,
            // if the node names exist on the hashmap. If true, adds node to the queue
            if (!currentNode.getIncoming().isEmpty()) {
                for (Edge predecessors : currentNode.getIncoming()) {
                    predecessorNode = predecessors.getFrom();
                    allSuccessorsCalculated = true;
                    for (Edge pSuccessors : predecessorNode.getOutgoing()) {
                        if (!bottomLevels.containsKey(pSuccessors.getTo().getName())) {
                            allSuccessorsCalculated = false;
                        }
                    }
                    if (allSuccessorsCalculated) {
                        queuedNodes.add(predecessorNode);
                    }
                }
            }
        }
        return bottomLevels;
    }

    /**
     * Function to calculate and update the work for a partialSolution aka the cost function f(s)
     * @param ps a partial solution
     */
    public void calculateUnderestimate(PartialSolution ps) {


        // update idle time heuristic TODO: optimise
        int idleTimeHeuristic = _idleConstantHeuristic + ps._idleTime / _numberOfProcessors;

        // data ready time heuristic
        int dataReadyTimeHeuristic = calculateDataReadyTime(ps);

        // update estimate
        ps._cost = Math.max(Math.max(Math.max(ps._bottomLevelWork, ps._cost), idleTimeHeuristic), dataReadyTimeHeuristic);
    }

    /**
     * Third component of the proposed cost function f(s)
     * calculates the data ready time (DRT) of a node n_j on processor p
     * @return
     */
    public int calculateDataReadyTime(PartialSolution ps){

        List<Node> freeNodeList = getFreeNodes(ps);
        int maximumDRT = 0;

        for(Node freeNode : freeNodeList){
            // get minimum drt on each processor
            int minDrt = -1;
            int blw = _bottomLevelWork.get(freeNode.getName());
            for (int i : earliestTimeOnProcessors(ps, freeNode)) {
                if (i < minDrt || minDrt == -1) {
                    minDrt = i;
                }
            }
            int dataReadyFinish = blw + minDrt;
            if (dataReadyFinish > maximumDRT) maximumDRT = dataReadyFinish;
        }
        return maximumDRT;
    }

    private List<Node> getFreeNodes(PartialSolution parentPS) {
        List<Node> freeNodes = new ArrayList<Node>();
        List<Node> nodes = _graph.getNodes();
        //get all the free variables from list of all nodes in graph
        for (Node node : nodes) {
            if (parentPS._nodes.contains(node.toString())) {
            } else {
                boolean hasMissingSuccessor = false;
                for (Edge e : node.getIncoming()) {
                    if (!parentPS._nodes.contains(e.getFrom().getName())) {
                        hasMissingSuccessor = true;
                    }
                }
                if (!hasMissingSuccessor) {
                    freeNodes.add(node);
                }
            }
        }
        return freeNodes;
    }

    /**
     * This finds the earliest time on each processor that a node can be scheduled to
     * based on successor nodes and the latest slot finishing time on each processor.
     * @param parentPS
     * @param freeNode
     * @return int[] index is the processor, value is the time
     */
    private int[] earliestTimeOnProcessors(PartialSolution parentPS, Node freeNode) {
        int[] earliestTimes = new int[_numberOfProcessors];
        ArrayList<Node> parents = freeNode.getParentNodes();
        int[] maxPredecessorTime = new int[_numberOfProcessors];
        int maxTime = 0;
        ProcessorSlot maxSlot = null;
        // iterate through each processor and check for successor nodes. Find the maximum time and edge time (for transfer)
        for (int i = 0; i < _numberOfProcessors; i++) {
            ArrayList<ProcessorSlot> processor = parentPS._processors[i];
            for (int j = processor.size() - 1; j >= 0; j--) {
                ProcessorSlot slot = processor.get(j);
                if (parents.contains(slot.getNode())) { // slot contains a predecessor
                    int slotProcessor = slot.getProcessor();
                    Edge parentEdge = _graph.getEdge(slot.getNode().getId(), freeNode.getId());
                    int parentTime = parentEdge.getWeight() + slot.getFinish();
                    if (parentTime > maxPredecessorTime[slotProcessor]) { // can only be max if it was at least greater than the prev one in processor
                        maxPredecessorTime[slotProcessor] = parentTime;
                        if (parentTime > maxTime) {
                            maxTime = parentTime;
                            maxSlot = slot;
                        }
                    }
                }
            }
        }

        //DEBUG
        if (maxSlot == null) { // no predecessor constraints, we can schedule as early as possible on each processor based on their last slot
            for (int i = 0; i < _numberOfProcessors; i++) {
                ProcessorSlot latestSlot = parentPS._latestSlots[i];
                if (latestSlot == null) {
                    earliestTimes[i] = 0; // there is no slot on the processor, we can start at 0
                } else {
                    earliestTimes[i] = latestSlot.getFinish();
                }
            }
            return earliestTimes;
        } else { // predecessor constraint is there, we can schedule at earliest maxSlot.finishTime + maxEdge
            // we need to find the second maxSlot for predecessor constraints on the maxSlotProcessor
            int maxSuccessorProcessor = maxSlot.getProcessor();
            int secondMaxSuccessorTime = 0;
            ProcessorSlot finalSlot;
            int finalSlotTime = 0;
            for (int i = 0; i < _numberOfProcessors; i++) {
                finalSlot = parentPS._latestSlots[i];
                finalSlotTime = 0;
                if (finalSlot != null) finalSlotTime = finalSlot.getFinish();
                earliestTimes[i] = Math.max(finalSlotTime, maxTime);
                if (maxPredecessorTime[i] > secondMaxSuccessorTime && i != maxSuccessorProcessor) {
                    secondMaxSuccessorTime = maxPredecessorTime[i];
                }
            }
            // we need to check predecessor constraints on other processors for the maxSuccessorProcessor slot
            finalSlot = parentPS._latestSlots[maxSuccessorProcessor];
            finalSlotTime = 0;
            if (finalSlot != null) finalSlotTime = finalSlot.getFinish();
            earliestTimes[maxSuccessorProcessor] = Math.max(secondMaxSuccessorTime, finalSlotTime);
            return earliestTimes;
        }
    }

    /**
     * check if a node is present within the schedule of the partial schedule.
     * @param node
     * @return
     */
    protected boolean contains(PartialSolution ps, Node node){
        for (ArrayList<ProcessorSlot> processor : ps._processors){
            if (processor.contains(node)){
                return true;
            }
        }
        return false;
    }

    /**
     * Add a slot to a processor, updating latestSlots, latestSlot and idleTime as necessary
     * @param slot
     */
    public void addSlot(PartialSolution ps, ProcessorSlot slot) {
        ProcessorSlot latestSlot = ps._latestSlots[slot.getProcessor()];
        int prevSlotFinishTime;
        if (latestSlot == null) { // this is the first slot in the processor
            ps._id.put(slot.getNode().getId(), slot.getProcessor());
            prevSlotFinishTime = 0;
        } else {
            prevSlotFinishTime = latestSlot.getFinish();
        }
        int processor = slot.getProcessor();
        ps._processors[processor].add(slot);
        ps._idleTime += slot.getStart() - prevSlotFinishTime; // add any idle time found
        ps._bottomLevelWork = Math.max(ps._bottomLevelWork, slot.getStart() + _bottomLevelWork.get(slot.getNode().getName()));// update max bottom level work
        ps._latestSlots[processor] = slot; // the newest slot becomes the latest
        ps._nodes.add(slot.getNode().getName()); // add node to node string
        if (ps._latestSlot == null || ps._latestSlot.getFinish() < slot.getFinish()) {
            ps._latestSlot = slot; // last slot across all processors is the new slot if it finishes later
        }
    }

    public void checkAndAdd(PartialSolution ps, PSPriorityQueue queue) {
        if (_cache.add(ps)) {
            queue.add(ps);
        }
    }
}
