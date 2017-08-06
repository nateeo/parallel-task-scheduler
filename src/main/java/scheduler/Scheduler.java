package scheduler;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import logger.Logger;

/**
 * Entry point to the scheduling algorithm
 */
public class Scheduler {

    @Parameter(names="INPUT.dot", required = true)
    private static String _inputFile;

    @Parameter(names="P", required = true)
    private static int _processors;

    @Parameter(names = "-p", description = "Number of cores")
    private static int _cores = 1;

    @Parameter(names = "-v", description = "Visualize scheduling")
    private static boolean _visualize = false;

    @Parameter(names = "-o", description = "Output filename")
    private static String _outputFile = "INPUT-output.dot";



    /**
     * Command line entry for the algorithm
     * @param args
     */
    public static void main(String[] args) {
        Logger.startTiming();
        System.out.println("Hello world");
        try {
            parseConsole(args);
        } catch (InvalidInputException e) {
            System.out.println();
            e.printStackTrace();

        }

        Logger.endTiming();
    }

    private static void parseConsole(String[] args) throws InvalidInputException{
        if((2<args.length)||(args.length>5)){
            throw new InvalidInputException("Invalid number of arguments.");
        }
        JCommander.newBuilder().addObject(args).build().parse(args);

        System.out.println(_inputFile+ _cores+ _outputFile+ _processors+ _visualize);
    }

}
