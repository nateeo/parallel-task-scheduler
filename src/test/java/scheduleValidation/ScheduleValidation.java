package scheduleValidation;

import graph.Graph;
import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
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
        //TO DELETE LATER
        //:PartialSolution
        //int _idleTime
        //int _cost
        //ArrayList<ProcessorSlot>[] _processors (array of array lists, length no. processors)
        //ProcessorSlot _processorSlot (represents a task that is on the processor)
        //contains(Node node)

        ArrayList<ProcessorSlot>[] processors = ps.getProcessors();

        // Generate a list of ProcessorSlot objects and sort based on their start times in the proposed schedule
        //Topological sort
        ArrayList<ProcessorSlot> sortedProcessorSlots = sortPartialSolutionNodes(processors);

        // if a node is scheduled before its dependencies -eli
        boolean test1 = checkOrder();

        //if a length of task is not equal to the weight of a node
        boolean test2 = checkWeight();

        // switching time not correct (sounds hard)
        boolean test3 = lol();

        //only one task is active on every processor
        boolean test4 = lol123();

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

    public boolean checkOneActive(ArrayList<ProcessorSlot> processors) {

    }

}
