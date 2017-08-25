package scheduler;


import algorithm.PSManager;
import algorithm.PSManagerWrapper;
import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import dotParser.Parser;
import frontend.Listener;
import frontend.Main;
import frontend.ScheduleGraphGenerator;
import graph.Graph;
import javafx.application.Application;
import logger.Logger;
import parallelization.Parallelization;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

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
    private static PSManager _psManager;

    // for visualisation
    private static int DELAY_TIME = 3000;
    private static int REFRESH_TIME = 1000;


    public static Timer _updater;

    private static PartialSolution _last;

    public static Listener _listener;

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
        PartialSolution ps = null;

        Timer updater = new Timer();

        if(_visualize) {

            new Thread(() -> {
                Application.launch(Main.class);
            }).start();
            new Thread(() -> {
                TimerTask task = new TimerTask() {
                    public void run() {
                        PartialSolution ps = _priorityQueue._queue.peek();
                            if (ps != null) {
                                _last = ps;
                            }
                            if (_last != null) {
                                PartialSolution currentBestPS = Scheduler._priorityQueue._queue.peek();
                                ScheduleGraphGenerator sgm = new ScheduleGraphGenerator(currentBestPS);
                                if (_listener != null) {
                                    _listener.notify("Updated", currentBestPS);
                                }
                            }
                        int[] nodevizCounts = _psManager._nodeVisitCounts;
                            int memory = _psManager._memory;
                            int cost = _psManager._cost;
                            int statesExplored = ps.
                            if (_listener != null) {
                                _listener.update(_psManager._nodeVisitCounts, _psManager._memory, _psManager._cost, _psManager._currentFinishTime, _psManager._statesExplored));
                            }
                    }
                };
                updater.schedule(task, DELAY_TIME, REFRESH_TIME);
            }).start();
        }


        // PSManager instance to perform calculations and generate states from existing Partial Solutions
        if(_visualize){
            _psManager = new PSManagerWrapper(_processors, _graph);
        } else {
            _psManager = new PSManager(_processors, _graph);
        }

        //priority queue will terminate upon the first instance of a total solution
        while (_priorityQueue.hasNext()) {
            if (_parallelOn == false || _priorityQueue.size() <= 1000) {
                ps = _priorityQueue.getCurrentPartialSolution();
                //generate the child partial solutions from the current "best" candidate partial solution
                //then add to the priority queue based on conditions.
                _psManager.generateChildren(ps, _priorityQueue);
            } else {
                parallelization = true;
                Parallelization parallelize = new Parallelization(_priorityQueue, _processors, _graph, _cores, _psManager.getCache());
                ps = parallelize.findOptimal();
                break;
            }


        }
        if (!parallelization){
            ps = _priorityQueue.getCurrentPartialSolution();
        }
        // kill timer
//        updater.cancel();
//        updater.purge();
//        System.out.println("killed updater");
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

