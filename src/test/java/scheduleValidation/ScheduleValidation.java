package scheduleValidation;

import graph.Edge;
import graph.Graph;
import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
import graph.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class that can be leveraged to check if a solution is valid based on an input graph
 */
public class ScheduleValidation {

    /**
     *
     * @param graphIn, the input .dot file represented as a Graph object.
     * @param ps, the PartialSolution object representing the schedule we wish to validate
     * @return boolean, true: iff the schedule is a valid schedule based on the input graph graphIn, false: otherwise
     * @author Eli Salter, Zihao Yang
     */
    public static boolean scheduleIsValid(Graph graphIn, PartialSolution ps){
        //TO DELETE LATER
        //:PartialSolution
        //int _idleTime
        //int _cost
        //ArrayList<ProcessorSlot>[] _processors (array of array lists, length no. processors)
        //ProcessorSlot _processorSlot (represents a task that is on the processor)
        //contains(Node node)

        // nodes in the partial solution, in their respective processors according the the array
        ArrayList<ProcessorSlot>[] processors = ps.getProcessors();

        boolean[] valid = new boolean[4];

        // Generate a list of ProcessorSlot objects and sort based on their start times in the proposed schedule
        //Topological sort
        ArrayList<ProcessorSlot> sortedProcessorSlots = sortPartialSolutionNodes(processors);

        // if a node is scheduled before its dependencies -eli
        valid[0] = checkOrder(graphIn, sortedProcessorSlots);

        //if a length of task is not equal to the weight of a node
        valid[1] = checkWeight(processors, graphIn);

        // switching time not correct (sounds hard)
        valid[2] = checkSwitchingTime(processors, graphIn);

        //only one task is active on every processor
        valid[3] = checkOneActive(processors);

        int fail_count = 0;

        for (int i = 0; i < valid.length; i++) {

            if (valid[i] == false) {
                System.out.println("method " + i + " failed");
                fail_count++;
            } else {
                System.out.println("method" + i + " success");
            }
        }

        if(fail_count == 0){
            return true;
        }
        return false;

    }

    /**
     *
     * @param psIn, an array of ProcessorSlot ArrayLists representing all the tasks in our schedule we wish to sort
     * @return
     */
    private static ArrayList<ProcessorSlot> sortPartialSolutionNodes(ArrayList<ProcessorSlot>[] psIn){

        ArrayList<ProcessorSlot> sortedProcessorList = new ArrayList<ProcessorSlot>();

        // For every processor in the array
        for(int i = 0; i < psIn.length; i++){

            // for every node in the processor
            for(int j = 0; j < psIn[i].size(); j++){
                sortedProcessorList.add(psIn[i].get(j));
            }
        }

        Collections.sort(sortedProcessorList, new Comparator<ProcessorSlot>() {
            @Override
            public int compare(ProcessorSlot p1, ProcessorSlot p2){
                return p1.getStart() - p2.getStart();
            }
        } );

        return sortedProcessorList;
    }


    private static boolean checkWeight(ArrayList<ProcessorSlot>[] processors, Graph graph) {

        boolean valid = true;

        for (ArrayList<ProcessorSlot> singleProcessor : processors) {
            for (ProcessorSlot slot : singleProcessor) {
                for (Node node : graph.getNodes()) {
                    if (slot.getNode().equals(node)) {

                        int slotTime = slot.getFinish() - slot.getStart();
                        int nodeWeight = node.getWeight();

                        if (slotTime != nodeWeight) {
                            valid = false;

                            System.out.println("the weight of task: " + node.toString() + " does not match the time the task runs");
                            break;
                        }
                    }
                }
            }

        }

        return valid;

    }


    private static boolean checkOrder(Graph gIn, ArrayList<ProcessorSlot> sortedProcessorSlots){
        //For every Node in the graph
        for(Node n : gIn.getNodes()){
            //check that for all its predecessors, the index for which they're in the sortedProcessorsSlots array
            // is not greater than the index of the current node

            int positionOfCurrentNode = getNodeIndex(sortedProcessorSlots, n);

            // if the current node is in the sortedProcessorSlots array generated from the partial solution
            if(positionOfCurrentNode != -1){
                // for all the predecessors
                for(Edge incoming : n.getIncoming()){
                    int positionOfPredecessor = getNodeIndex(sortedProcessorSlots, incoming.getFrom());

                    //if the position of the predecessor is greater than the position of the current node, i.e
                    //the node has started before its predecessor, then return false
                    if(positionOfPredecessor > positionOfCurrentNode){
                        return false;
                    }
                }

            }

        }
        return true;
    }

    private static int getNodeIndex(ArrayList<ProcessorSlot> sortedProcessorSlots, Node n){
        // this finds the index of the current node in the sortedProcessorSlots array
        for(int i = 0; i<sortedProcessorSlots.size(); i++){
            if(sortedProcessorSlots.get(i).getNode().equals(n)){
                return i;
            }
        }

        //match not found, so return the index -1
        return -1;
    }


    public static boolean checkOneActive(ArrayList<ProcessorSlot>[] processors) {

        boolean valid = true;

        for (ArrayList<ProcessorSlot> singleProcessor: processors) {
            for (ProcessorSlot slot: singleProcessor) {
                for (ProcessorSlot slot2: singleProcessor) {
                    if (!slot.getNode().equals(slot2.getNode())) {
                        if ((slot2.getStart() > slot.getStart()) && (slot2.getFinish() < slot.getFinish())) {
                            valid = false;

                            System.out.println(slot.getNode().getName() + " is active at the same time as " + slot2.getNode().getName());
                            break;
                        }
                    }
                }
            }
        }

        return valid;
    }

    public static boolean checkSwitchingTime(ArrayList<ProcessorSlot>[] processors, Graph graph) {

        boolean valid = true;

        for (ArrayList<ProcessorSlot> singleProcessor: processors) {
            for (ProcessorSlot slot : singleProcessor) {
               ArrayList<Node> parentNodes = slot.getNode().getParentNodes();
                for (ArrayList<ProcessorSlot> singleProcessor1: processors) {
                    for (ProcessorSlot parentSlot : singleProcessor1) {
                        if (parentSlot.getProcessor() != slot.getProcessor()) {
                            Node node = parentSlot.getNode();
                            if (parentNodes.contains(node)) {
                                int wait = slot.getStart() - parentSlot.getFinish();
                                int edge = graph.getEdge(new Edge(node, slot.getNode(), 0)).getWeight();

                                if (wait > edge) {
                                    valid = false;
                                    System.out.println("the switching time between " + parentSlot.getNode().getName() + " and " + slot.getNode().getName() + "is incorrect");
                                }
                            }
                        }

                    }
                }
            }


        }

        return valid;
    }

}
