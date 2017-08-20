package scheduleValidation;

import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
import graph.Edge;
import graph.Graph;
import graph.Node;

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

        // get the processors used in the solution, containing tasks
        ArrayList<ProcessorSlot>[] processors = ps.getProcessors();

        // if all values in the array is true at the end, then schedule is valid
        boolean[] valid = new boolean[4];

        // Tests whether nodes appear after all their dependencies
        valid[0] = checkOrder(graphIn, processors);

        // Tests if a length of tasks is equal to the weights of their respective nodes
        valid[1] = checkWeight(processors, graphIn);

        // Tests if switching time is the same as edge weights
        valid[2] = checkSwitchingTime(processors, graphIn);

        // Tests that only one task is active on a processor at any given time
        valid[3] = checkOneActive(processors);

        int fail_count = 0;

        for (int i = 0; i < valid.length; i++) {

            if (valid[i] == false) {
                System.out.println("method" + i + " failed");
                fail_count++;
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
     * @return ArrayList of ProcessorSlot, sorted by time
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


    /**
     * Tests whether the runtime of tasks are the same as the node weights
     * @param processors list of processors containing all the scheduled tasks
     * @param graph input graph to be scheduled
     * @return
     */
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

                        }
                    }
                }
            }

        }

        return valid;

    }


    /**
     *
     * @param gIn graph of input tasks to be scheduled
     * @param processors processors containing the scheduled tasks
     * @return
     */
    private static boolean checkOrder(Graph gIn, ArrayList<ProcessorSlot>[] processors){

        // Generate a list of ProcessorSlot objects and sort based on their start times in the proposed schedule
        //Topological sort
        ArrayList<ProcessorSlot> sortedProcessorSlots = sortPartialSolutionNodes(processors);

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

    /**
     * Check if there is only one active task on each processor at any time
     * @param processors processors containing the scheduled tasks
     * @return
     */
    public static boolean checkOneActive(ArrayList<ProcessorSlot>[] processors) {

        boolean valid = true;

        for (ArrayList<ProcessorSlot> singleProcessor: processors) {
            int maxTimeSeen = 0;

            Collections.sort(singleProcessor, new Comparator<ProcessorSlot>() {
                @Override
                public int compare(ProcessorSlot p1, ProcessorSlot p2){
                    return p1.getStart() - p2.getStart();
                }
            } );

            for (ProcessorSlot slot : singleProcessor) {
                if (slot.getStart() < maxTimeSeen) {
                    System.out.println("Task " + slot.getNode().getName() + " is colliding with another task in processor " + slot.getProcessor());
                    valid = false;
                }

                if (slot.getFinish() > maxTimeSeen) {
                    maxTimeSeen = slot.getFinish();
                }
            }

        }





        return valid;
    }

    /**
     * Checks if the switching times between the tasks in different processors are valid
     * @param processors processors containing the scheduled tasks
     * @param graph input graph of tasks to be scheduled
     * @return
     */
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
                                int edge = graph.getEdge(node.getId(), slot.getNode().getId()).getWeight();
                                if (wait < edge) {
                                    valid = false;
                                    System.out.println("the switching time between " + parentSlot.getNode().getName() + " and " + slot.getNode().getName() + " is incorrect");
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
