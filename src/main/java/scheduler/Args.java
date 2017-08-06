package scheduler;

/**
 * Created by edisonrho on 6/08/17.
 */


import com.beust.jcommander.Parameter;

public class Args {
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
}


