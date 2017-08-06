package scheduleValidation;

import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
import graph.Graph;

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


        // Generate a list of ProcessorSlot objects and sort based on their start times in the proposed schedule
        //Topological sort
        ArrayList<ProcessorSlot> sortedProcessorSlots = sortPartialSolutionNodes(ps._processors);

        // if a node is scheduled before its dependencies -eli
        boolean test1 = SOMEFUNCTIONNAMEHERE();

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
                return p1.
            }
        } );

        return sortedProcessorList;
    }


    private static boolean checkWeight(ps._processors, Graph graph) {


    }
}
