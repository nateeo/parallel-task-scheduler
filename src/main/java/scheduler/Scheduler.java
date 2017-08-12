package scheduler;


import algorithm.PSManager;
import algorithm.PSPriorityQueue;
import algorithm.PartialSolution;
import dotParser.Parser;
import graph.Graph;
import logger.Logger;

import java.io.File;

/**
 * Entry point to the scheduling algorithm
 */
public class Scheduler {
    private static String _inputFileName;
    private static int _processors;
    private static int _cores = 1;
    private static boolean _visualize = false;
    private static String _outputFile = "INPUT-output.dot";
    private static File _inputFile;
    private static Graph _graph;

    private static String _consolePrefix = "(Hi-5 Scheduler v1.0)\t";


    /**
     * Command line entry for the algorithm
     *
     * @param args
     */
    public static void main(String[] args) {
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

    public static PartialSolution parseConsole(String[] args) throws InvalidInputException, Exception {
        int argLength = args.length;
        if ((argLength < 2) || (argLength > 7)) {
            throw new InvalidInputException("Invalid number of arguments.");
        }
        _inputFileName = args[0];
        if (!_inputFileName.endsWith(".dot")) {
            throw new InvalidInputException("Input file must be dot");
        }
        _inputFile = new File(_inputFileName);
        _processors = Integer.valueOf(args[1]);


        for (int i = 2; i < argLength; i++) {
            switch (args[i]) {
                case "-p":
                    _cores = Integer.valueOf(args[i + 1]);

                    break;
                case "-v":
                    _visualize = true;
                    break;
                case "-o":
                    _outputFile = args[i + 1];
                    if (!_outputFile.endsWith(".dot")) {
                        throw new InvalidInputException("output file must be dot");
                    }

            }
        }
        _graph = Parser.parseDotFile(_inputFile);
        Logger.startTiming();
        System.out.println(_consolePrefix + "Processing the graph...");
        PartialSolution ps = solution();
        long totalTime = Logger.endTiming();
        if (ps == null) {
            Logger.error("null solution. Are you sure this is a valid task graph?");
        }
        System.out.println(_consolePrefix + "Found a schedule to " + _graph.getName() + " (" + _graph.getNodes().size() + " nodes) in " + totalTime + "ms.");
        System.out.println(_consolePrefix + "End time of this schedule is " + ps._cost + ".");
        System.out.println(_consolePrefix + "Outputting to file \"" + _outputFile + "\"...");
        parseOutput(ps); // output to file
        System.out.println(_consolePrefix + "Finished!");
        return ps; // for testing
    }

    private static PartialSolution solution() {
        // Priority queue containing generated states
        PSPriorityQueue priorityQueue = new PSPriorityQueue(_graph, _processors);

        // PSManager instance to perform calculations and generate states from existing Partial Solutions
        PartialSolution ps = null;
        PSManager psManager = new PSManager(_processors, _graph);
        while (priorityQueue.hasNext()) {
            ps = priorityQueue.getCurrentPartialSolution();
            psManager.generateChildren(ps, priorityQueue);
        }
        ps = priorityQueue.getCurrentPartialSolution();
        return ps;
    }

    private static void parseOutput(PartialSolution ps){
        Parser.outputGraphToFile(ps,_outputFile,_inputFile);
    }

}
