package scheduleValidation;

import graph.Edge;
import graph.Graph;
import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
import graph.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that can be leveraged to check if a solution is valid based on an input graph
 */
public class ScheduleValidation {

    /**
     *
     * @param graphIn, the input .dot file represented as a Graph object.
     * @param ps, the PartialSolution object representing the schedule we wish to validate
     * @return boolean, true: iff the schedule is a valid schedule based on the input graph graphIn, false: otherwise
     * @author Eli Salter
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

        // Generate a list of ProcessorSlot objects and sort based on their start times in the proposed schedule
        //Topological sort


        ArrayList<ProcessorSlot> sortedProcessorSlots = sortPartialSolutionNodes(ps._processors);

        boolean[] individualResults = new boolean[4];

        // if a node is scheduled before its dependencies -eli
        individualResults[0] = SOMEFUNCTIONNAMEHERE();

        //if a length of task is not equal to the weight of a node
        individualResults[1] = checkWeight(processors, graphIn);

        // switching time not correct (sounds hard)
        individualResults[2] = checkSwitchingTime(processors, graphIn);

        //only one task is active on every processor
        individualResults[3] = checkOneActive(processors);



        for (int i =0; i < individualResults.length; i++) {
            if (individualResults[i] == false) {
                return false
            }
        }

        return true;
    }

    /**
     *
     * @param psIn, an array of ProcessorSlot ArrayLists representing all the tasks in our schedule we wish to sort
     * @return
     */
    private static ArrayList<ProcessorSlot> sortPartialSolutionNodes(ArrayList<ProcessorSlot>[] psIn){

        ArrayList<ProcessorSlot> sortedProcessorList = new ArrayList<ProcessorSlot>();

        // For every processor
        for(int i = 0; i < psIn.length; i++){
            // for every node
            for(int j = 0; j < psIn._processors[i].length(); j++){
                sortedProcessorList.add(psIn._processor[i].get(j));
            }
        }

        Collections.sort(sortedProcessorList, new Comparator<ProcessorSlot>() {
            @Override
            public int compare(ProcessorSlot p1, ProcessorSlot p2){
                return p1.get
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
                    for (ProcessorSlot parentSlot : singleProcessor) {
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
