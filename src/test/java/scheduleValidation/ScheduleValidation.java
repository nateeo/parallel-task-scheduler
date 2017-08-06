package scheduleValidation;

/**
 * Class that can be leverage to check if a solution is valid based on an input graph
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
        ArrayList<ProcessorSlot> sortedProcessorSlots = new ArrayList<ProcessorSlot>();

        // For every processor
        for(int i = 0; i < ps._processors.length; i++){
            // for every node
            for(int j = 0; j < ps._)

        }





        return false;
    }

    /**
     * 
     * @param psIn
     * @return
     */
    public ArrayList<ProcessorSlot> sortPartialSolutionNodes(ArrayList<ProcessorSlot>[] psIn){

    }
}
