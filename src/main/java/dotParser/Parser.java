package dotParser;

import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains methods to:
 * parse .dot files to a JGraph data structure
 * generate .dot files from a JGraph data structure
 */
public class Parser {

    Boolean[] inputFlagArray;

    /**
     * Parses .dot file and returns a JGraph representation.
     * @param file
     * @return Graph, null if there was an error reading the file
     * @throws IOException
     */
    public static Graph parseDotFile (File file) {
        HashMap<String, Node> nodeMap = new HashMap<String, Node>();
        HashMap startNodes;
        Graph graph;
        int totalMinimumWork = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Parse graph lines
        String line;
        String[] splitLine;
        ArrayList<String[]> arcQueue = new ArrayList<String[]>();
        try {
            String graphName = br.readLine().split("\"")[1];
            graph = new Graph(graphName);

            while (!(line = br.readLine()).equals("}")) {
                splitLine = line.split("\\[");
                String left = splitLine[0].trim();
                String right = splitLine[1];
                int weight;

                if (splitLine[0].trim().length() == 1) { // add single vertex to graph and hashmap, as well as weight to min work
                    weight = getValue(right);
                    Node newVertex = new Node(left, weight);
                    totalMinimumWork += weight;
                    nodeMap.put(left, newVertex);
                } else { // add arc to queue for processing at the end
                    arcQueue.add(new String[]{left, right});
                }
            }
        } catch (IOException e) {
            return null;
        }

        // clone nodeMap to find possible starting nodes
        startNodes = new HashMap<String, Node>(nodeMap);
        // process arcQueue
        for (String[] string : arcQueue) {
            String[] arcString = string[0].split("->");
            String from = arcString[0].trim();
            String to =  arcString[1].trim();
            Node fromNode = nodeMap.get(from);
            Node toNode = nodeMap.get(to);
            int weight = getValue(string[1]);
            graph.addEdge(new Edge(fromNode, toNode, weight));
            fromNode.addOutgoingEdge(toNode, weight);
            toNode.addIncomingEdge(fromNode, weight);
            startNodes.remove(nodeMap.get(to).getName());
        }

        // set some useful fields in graph Object
        graph.setStart(new ArrayList<Node>(startNodes.values()));
        graph.setNodes(new ArrayList<Node>(nodeMap.values()));
        graph.setTotalMinimumWork(totalMinimumWork);

        return graph;
    }

    /**
     * Outputs a graph to a .dot file of the specified outputFile parameter.
     *
     */
    public static void outputGraphToFile(PartialSolution finalSolution, String outputFile, File inputFile) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //store the input file into a string arraylist of lines.
        ArrayList<String> outputArray = new ArrayList<String>();
        StringBuilder output = new StringBuilder();

        try {

            String line = br.readLine();
            //read first line, which contains the name of the digraph.
            //modify the first line to append output to the front of the digraph name, as well as
            //capitalize the first letter of the name of the digraph.
            String[] firstLineArray = line.split(" ");
            firstLineArray[2] = "\"output"+firstLineArray[2].substring(1,2).toUpperCase()+firstLineArray[2].substring(2);
            output.append("digraph " + firstLineArray[2] +" {\n");

            //populate the array with lines of the input
            while (!(line = br.readLine()).equals("}")) {
                outputArray.add(line);
            }
            //for each line, if it is a task, then check the PartialSolution to obtain the process number and ProcessSlots
            //to find the start time for that task.
            //if it is an edge, then output it directly without any modifications.
            for (String outputLine : outputArray){
                String[] splitLine = outputLine.split("\\[");
                String taskName = splitLine[0].trim();
                String right = splitLine[1];
                int weight;

                if (splitLine[0].trim().length() == 1) {
                    weight = getValue(right);
                    for (ArrayList<ProcessorSlot> processor: finalSolution.getProcessors()){
                        for (ProcessorSlot processorSlot: processor){
                            if (processorSlot.getNode().getName().equals(taskName)){
                                String taskOutput = "\t"+taskName+"\t\t\t\t"+"[ Weight ="+weight
                                        +",Start ="+processorSlot.getStart()
                                        +",Processor ="+(processorSlot.getProcessor()+1)+"];\n";
                                output.append(taskOutput);
                            }
                        }
                    }

                } else { // add arc to queue for processing at the end
                    output.append(outputLine+"\n");
                }
            }
            output.append("}\n");

            try {
                //output to file
                PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
                writer.print(output.toString());
                writer.close();
            } catch (IOException e) {
                System.out.println("Invalid outout file name. ");
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static int getValue(String value) {
        return Integer.parseInt(value.substring(value.indexOf("=") + 1, value.lastIndexOf("]")).trim());
    }
}
