package scheduler;


import algorithm.Cache;
import algorithm.PSManager;
import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import dotParser.Parser;
import frontend.Main;
import graph.Graph;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import logger.Logger;
import parallelization.Parallelization;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.TimerTask;

import static scheduler.Scheduler._priorityQueue;

/**
 * Entry point to the scheduling algorithm
 */
public class Scheduler {
    private static String _inputFileName;
    public static int _processors;
    private static int _cores = 1;
    public static boolean _visualize = false;
    private static String _outputFile = "INPUT-output.dot";
    private static File _inputFile;
    public static Graph _graph;
    public static PSPriorityQueue _priorityQueue;
    private static String[] _args;

    private static String _consolePrefix = "(Hi-5 A* Scheduler v2.0)\t";

    private static boolean _parallelOn = false;


    /**
     * Command line entry for the algorithm
     *
     * @param args
     */
    public static void main(String[] args) {
        _args = args;
        try {
            parseConsole(args);
        } catch (InvalidInputException e) {
            System.out.println();
            e.printStackTrace();

        } catch (Exception e) {
            System.out.println("Invalid input");
            e.printStackTrace();
        }
    }

    /**
     * Parses the console based on the arguments given in the console.
     * @param args the console input
     * @return the final optimal solution
     * @throws InvalidInputException
     * @throws Exception
     */
    public static PartialSolution parseConsole(String[] args) throws InvalidInputException, Exception {
        int argLength = args.length;
        boolean customOutput = false;
        //argument must have at most 7 space separated strings.
        //mandatory arguments require 2 space separated strings.
        if ((argLength < 2) || (argLength > 7)) {
            throw new InvalidInputException("Invalid number of arguments.");
        }
        //first string is input filename. Must be a .dot file.
        _inputFileName = args[0];
        if (!_inputFileName.endsWith(".dot")) {
            throw new InvalidInputException("Input file must be dot");
        }
        _inputFile = new File(_inputFileName);
        //second string is the number of processors on which to schedule tasks.
        //NB this is different to the number of threads to run in parallelization.
        _processors = Integer.valueOf(args[1]);

        //loop through the rest of the arguments.
        for (int i = 2; i < argLength; i++) {
            switch (args[i]) {
                case "-p":
                    _cores = Integer.valueOf(args[i + 1]);

                    if (_cores > 1) {
                        _parallelOn = true;
                    }
                    break;
                case "-v":
                    _visualize = true;
                    break;
                case "-o":
                    _outputFile = args[i + 1];
                    customOutput = true;
            }
        }
        //use the parser to generate a graph from the input .dot file.
        _graph = Parser.parseDotFile(_inputFile);
        //if user did not define an output file name, make a default one based on graph name.
        if (!customOutput){
            _outputFile = _graph.getName()+"-output.dot";//generate output based on graph name
        }
        Logger.startTiming();
        System.out.println(_consolePrefix + "Processing the graph...");
        PartialSolution ps = solution();//generate the solution
        long totalTime = Logger.endTiming();
        if (ps == null) {
            Logger.error("null solution. Are you sure this is a valid task graph?");
        }
        System.out.println(_consolePrefix + "Found a schedule to " + _graph.getName() + " (" + _graph.getNodes().size() + " nodes) in " + totalTime + "ms.");
        System.out.println(_consolePrefix + "End time of this schedule is " + ps._latestSlot.getFinish() + ".");
        System.out.println(_consolePrefix + "Outputting to file \"" + _outputFile + "\"...");
        parseOutput(ps); // output to file
        System.out.println(_consolePrefix + "Finished!");
        System.out.println(ps.toString());
        return ps; // for testing
    }

    /**
     * Generate the valid optimal schedule based on the generated input graph
     * and the number of processors on which to schedule.
     * @return the valid optimal schedule
     */
    private static PartialSolution solution() throws ExecutionException, InterruptedException {
        // Priority queue containing generated states
         _priorityQueue = new PSPriorityQueue(_graph, _processors);
        _priorityQueue.initialise();
        Boolean parallelization = false;

        if(_visualize) {
            new Thread() {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        javafx.application.Application.launch(Main.class);
                    }

                };

            }.start();
        }


        // PSManager instance to perform calculations and generate states from existing Partial Solutions
        PartialSolution ps = null;
        PSManager psManager = new PSManager(_processors, _graph);
        //priority queue will terminate upon the first instance of a total solution
        while (priorityQueue.hasNext()) {
            if (_parallelOn == false || priorityQueue.size() <= 1000) {
                ps = priorityQueue.getCurrentPartialSolution();
                //generate the child partial solutions from the current "best" candidate partial solution
                //then add to the priority queue based on conditions.
                psManager.generateChildren(ps, priorityQueue);
            } else {
                parallelization = true;
                Parallelization parallelize = new Parallelization(priorityQueue, _processors, _graph, _cores, psManager.getCache());
                ps = parallelize.findOptimal();
                break;
            }


        }
        if (!parallelization){
            ps = priorityQueue.getCurrentPartialSolution();
        }
        return ps;
    }

    /**
     * Output the valid optimal schedule onto an output file.
     * @param ps the valid optimal schedule to feed into output parser
     */
    private static void parseOutput(PartialSolution ps){
        Parser.outputGraphToFile(ps,_outputFile,_inputFile);
    }

}

class PSPriorityQueueWrapper {
    Timeline _timeline;
    // Listeners to give EDS/Sam stats every second or so
    // Uses decorator/wrapper pattern to wrap PSManager and send data every poll
    // Wraps PSManager

    // method to assign sam/eds stuff as a listneer, and this method has a timer to send
    // PSManager's data every second or so

    PSPriorityQueueWrapper(){
        startEventTimer();
    }

    public void startEventTimer(){
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> bestPS()));
        _timeline = timeline;
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void cancelEventTimer(){
        _timeline.stop();
    }

    public PartialSolution bestPS(){
        return _priorityQueue.getCurrentPartialSolution();
    }
}
