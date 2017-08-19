package scheduler;//####[1]####
//####[1]####
import algorithm.PSManager;//####[4]####
import algorithm.PSPriorityQueue;//####[5]####
import algorithm.PartialSolution;//####[6]####
import dotParser.Parser;//####[7]####
import graph.Graph;//####[8]####
import logger.Logger;//####[9]####
import java.io.File;//####[11]####
//####[11]####
//-- ParaTask related imports//####[11]####
import pt.runtime.*;//####[11]####
import java.util.concurrent.ExecutionException;//####[11]####
import java.util.concurrent.locks.*;//####[11]####
import java.lang.reflect.*;//####[11]####
import pt.runtime.GuiThread;//####[11]####
import java.util.concurrent.BlockingQueue;//####[11]####
import java.util.ArrayList;//####[11]####
import java.util.List;//####[11]####
//####[11]####
/**
 * Entry point to the scheduling algorithm
 *///####[15]####
public class Scheduler {//####[16]####
    static{ParaTask.init();}//####[16]####
    /*  ParaTask helper method to access private/protected slots *///####[16]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[16]####
        if (m.getParameterTypes().length == 0)//####[16]####
            m.invoke(instance);//####[16]####
        else if ((m.getParameterTypes().length == 1))//####[16]####
            m.invoke(instance, arg);//####[16]####
        else //####[16]####
            m.invoke(instance, arg, interResult);//####[16]####
    }//####[16]####
//####[17]####
    private static String _inputFileName;//####[17]####
//####[18]####
    private static int _processors;//####[18]####
//####[19]####
    private static int _cores = 1;//####[19]####
//####[20]####
    private static boolean _visualize = false;//####[20]####
//####[21]####
    private static String _outputFile = "INPUT-output.dot";//####[21]####
//####[22]####
    private static File _inputFile;//####[22]####
//####[23]####
    private static Graph _graph;//####[23]####
//####[25]####
    private static String _consolePrefix = "(Hi-5 Scheduler v1.0)\t";//####[25]####
//####[33]####
    /**
     * Command line entry for the algorithm
     *
     * @param args
     *///####[33]####
    public static void main(String[] args) {//####[33]####
        try {//####[34]####
            parseConsole(args);//####[35]####
        } catch (InvalidInputException e) {//####[36]####
            System.out.println();//####[37]####
            e.printStackTrace();//####[38]####
        } catch (Exception e) {//####[40]####
            System.out.println("Invalid input");//####[41]####
            e.printStackTrace();//####[42]####
        }//####[43]####
    }//####[44]####
//####[53]####
    /**
     * Parses the console based on the arguments given in the console.
     * @param args the console input
     * @return the final optimal solution
     * @throws InvalidInputException
     * @throws Exception
     *///####[53]####
    public static PartialSolution parseConsole(String[] args) throws InvalidInputException, Exception {//####[53]####
        int argLength = args.length;//####[54]####
        boolean customOutput = false;//####[55]####
        if ((argLength < 2) || (argLength > 7)) //####[58]####
        {//####[58]####
            throw new InvalidInputException("Invalid number of arguments.");//####[59]####
        }//####[60]####
        _inputFileName = args[0];//####[62]####
        if (!_inputFileName.endsWith(".dot")) //####[63]####
        {//####[63]####
            throw new InvalidInputException("Input file must be dot");//####[64]####
        }//####[65]####
        _inputFile = new File(_inputFileName);//####[66]####
        _processors = Integer.valueOf(args[1]);//####[69]####
        for (int i = 2; i < argLength; i++) //####[72]####
        {//####[72]####
            switch(args[i]) {//####[73]####
                case "-p"://####[73]####
                    _cores = Integer.valueOf(args[i + 1]);//####[75]####
                    break;//####[76]####
                case "-v"://####[76]####
                    _visualize = true;//####[78]####
                    break;//####[79]####
                case "-o"://####[79]####
                    _outputFile = args[i + 1];//####[81]####
                    customOutput = true;//####[82]####
            }//####[82]####
        }//####[84]####
        _graph = Parser.parseDotFile(_inputFile);//####[86]####
        if (!customOutput) //####[88]####
        {//####[88]####
            _outputFile = _graph.getName() + "-output.dot";//####[89]####
        }//####[90]####
        Logger.startTiming();//####[91]####
        System.out.println(_consolePrefix + "Processing the graph...");//####[92]####
        PartialSolution ps = solution();//####[93]####
        long totalTime = Logger.endTiming();//####[94]####
        if (ps == null) //####[95]####
        {//####[95]####
            Logger.error("null solution. Are you sure this is a valid task graph?");//####[96]####
        }//####[97]####
        System.out.println(_consolePrefix + "Found a schedule to " + _graph.getName() + " (" + _graph.getNodes().size() + " nodes) in " + totalTime + "ms.");//####[98]####
        System.out.println(_consolePrefix + "End time of this schedule is " + ps._latestSlot.getFinish() + ".");//####[99]####
        System.out.println(_consolePrefix + "Outputting to file \"" + _outputFile + "\"...");//####[100]####
        parseOutput(ps);//####[101]####
        System.out.println(_consolePrefix + "Finished!");//####[102]####
        return ps;//####[103]####
    }//####[104]####
//####[111]####
    /**
     * Generate the valid optimal schedule based on the generated input graph
     * and the number of processors on which to schedule.
     * @return the valid optimal schedule
     *///####[111]####
    private static PartialSolution solution() {//####[111]####
        PSPriorityQueue priorityQueue = new PSPriorityQueue(_graph, _processors);//####[113]####
        PartialSolution ps = null;//####[116]####
        PSManager psManager = new PSManager(_processors, _graph);//####[117]####
        while (priorityQueue.hasNext()) //####[119]####
        {//####[119]####
            ps = priorityQueue.getCurrentPartialSolution();//####[120]####
            psManager.generateChildren(ps, priorityQueue);//####[123]####
        }//####[124]####
        ps = priorityQueue.getCurrentPartialSolution();//####[125]####
        return ps;//####[126]####
    }//####[127]####
//####[133]####
    /**
     * Output the valid optimal schedule onto an output file.
     * @param ps the valid optimal schedule to feed into output parser
     *///####[133]####
    private static void parseOutput(PartialSolution ps) {//####[133]####
        Parser.outputGraphToFile(ps, _outputFile, _inputFile);//####[134]####
    }//####[135]####
}//####[135]####
