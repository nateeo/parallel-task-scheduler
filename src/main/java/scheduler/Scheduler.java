package scheduler;


import logger.Logger;

import java.security.spec.ECField;

/**
 * Entry point to the scheduling algorithm
 */
public class Scheduler {
    private static String _inputFile;
    private static int _processors;
    private static int _cores = 1;
    private static boolean _visualize = false;
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

        } catch (Exception e){
            System.out.println("Invalid input");
            e.printStackTrace();
        }

        Logger.endTiming();
    }

    private static void parseConsole(String[] args) throws InvalidInputException, Exception {
        int argLength = args.length;
        if((argLength<2)||(argLength>7)){
            throw new InvalidInputException("Invalid number of arguments.");
        }
        _inputFile = args[0];
        if (!_inputFile.endsWith(".dot")){
            throw new InvalidInputException("Input file must be dot");
        }
        _processors = Integer.valueOf(args[1]);


        for (int i = 2; i < argLength; i++){
            switch (args[i]) {
                case "-p":
                    _cores = Integer.valueOf(args[i+1]);

                    break;
                case "-v":
                    _visualize = true;
                    break;
                case "-o":
                        _outputFile = args[i+1];
                        if (!_outputFile.endsWith(".dot")){
                            throw new InvalidInputException("output file must be dot");
                        }

            }
        }

    }



}
