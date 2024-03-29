package dotParser;

import algorithm.PartialSolution;
import algorithm.ProcessorSlot;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * This class contains methods to:
 * parse .dot files to a JGraph data structure
 * generate .dot files from a JGraph data structure
 */
public class Parser {

    static int idCounter = 1;

    /**
     * Parses .dot file and returns a Graph Object representation.
     * @param file
     * @return Graph, null if there was an error reading the file
     * @throws IOException
     */
    public static Graph parseDotFile (File file) {
        idCounter = 1;
        HashMap<String, Node> nodeMap = new HashMap<String, Node>();
        HashMap<Integer, ArrayList<Node>> equalCostNodes = new HashMap<>();
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
        String left;
        String right;
        try {
            String graphName = br.readLine().split("\"")[1];
            graph = new Graph(graphName);

            while (!(line = br.readLine()).equals("}")) {
                splitLine = line.split("\\[");
                if (splitLine.length == 2 && splitLine[1].contains("Weight")) {
                    left = splitLine[0].trim();
                    right = splitLine[1];
                    int weight;

                    if (!splitLine[0].contains("->")) { // add single vertex to graph and hashmap, as well as weight to min work
                        weight = getValue(right);
                        Node newVertex = new Node(idCounter++, left, weight);
                        // add node to hashmap to detect duplicates
                        if (!equalCostNodes.containsKey(weight)) {
                            equalCostNodes.put(weight, new ArrayList<>());
                        }
                        equalCostNodes.get(weight).add(newVertex);
                        totalMinimumWork += weight;
                        nodeMap.put(left, newVertex);
                    } else { // add arc to queue for processing at the end
                        arcQueue.add(new String[]{left, right});
                    }
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
            String to = arcString[1].trim();
            Node fromNode = nodeMap.get(from);
            Node toNode = nodeMap.get(to);
            int weight = getValue(string[1]);
            graph.addEdge(new Edge(fromNode, toNode, weight));
            fromNode.addOutgoingEdge(toNode, weight);
            toNode.addIncomingEdge(fromNode, weight);
            startNodes.remove(nodeMap.get(to).getName());
        }

        // set some useful fields in graph Object
        graph.setNodes(new ArrayList<Node>(nodeMap.values()));
        graph.setStart(new ArrayList<Node>(startNodes.values()));
        graph.setTotalMinimumWork(totalMinimumWork);

        // topologically sort for setting id in order
        idCounter = 1;
        List<Node> nodes = graph.getNodes();
        Stack<String> stack = new Stack<>();
        boolean[] visited = new boolean[nodes.size() + 1];

        for (Node node : nodes) {
            if (visited[node.getId()] == false) {
                topologicalSort(node, visited, stack);
            }
        }

        while (stack.empty() == false) {
            String name = stack.pop();
            nodeMap.get(name).setTopId(idCounter);
            idCounter++;
        }

        // calculate bottom level work before handling node equivalence
        graph.bottomLevelCalculator();
        for (ArrayList<Node> list : equalCostNodes.values()) {
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    Node first = list.get(i);
                    Node other = list.get(j);
                    if (first.getIncoming().size() == other.getIncoming().size() && first.getOutgoing().size() == other.getOutgoing().size()) {
                        boolean equal = true;
                        for (Edge e : first.getIncoming()) {
                            boolean found = false;
                            for (Edge e2 : other.getIncoming()) {
                                if (e.getFrom().equals(e2.getFrom()) && e.getWeight() == e2.getWeight()) found = true;
                            }
                            if (!found) {
                                equal = false;
                                break;
                            }
                        }
                        if (equal) {
                            for (Edge e : first.getOutgoing()) {
                                boolean found = false;
                                for (Edge e2 : other.getOutgoing()) {
                                    if (e.getTo().equals(e2.getTo()) && e.getWeight() == e2.getWeight()) found = true;
                                }
                                if (!found) {
                                    equal = false;
                                    break;
                                }
                            }
                            if (equal) { // equal, add edge between them so they don't appear together
                                if (first.getTopId() < other.getTopId()) {
                                    graph.addEdge(new Edge(first, other, 0));
                                    first.addOutgoingEdge(other, 0);
                                    other.addIncomingEdge(first, 0);
                                } else {

                                    graph.addEdge(new Edge(other, first, 0));
                                    other.addOutgoingEdge(first, 0);
                                    first.addIncomingEdge(other, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        return graph;
    }

    private static void topologicalSort(Node node, boolean visited[], Stack stack) {
        visited[node.getId()] = true;
        ArrayList<Edge> outgoing = node.getOutgoing();
        for (Edge e : outgoing) {
            Node to = e.getTo();
            if (!visited[to.getId()]) {
                topologicalSort(to, visited, stack);
            }
        }
        stack.push(node.getName());
    }

    /**
     * Outputs a graph to a .dot file of the specified outputFile parameter
     * using a BufferedReader in combination with a StringBuilder.
     * Certain assumptions have been made as to the style of the command line input.
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
            String[] firstLineArray = line.split("\"");
            firstLineArray[1] = "\"output"+firstLineArray[1].substring(0,1).toUpperCase()+firstLineArray[1].substring(1);
            output.append("digraph " + firstLineArray[1] +"\" {\n");

            //populate the array with lines of the input
            while (!(line = br.readLine()).equals("}")) {
                String[] splitLine = line.split("\\[");
                if(splitLine.length == 2 && splitLine[1].contains("Weight")) {
                    outputArray.add(line);
                }
            }
            //for each line, if it is a task, then check the PartialSolution to obtain the process number and ProcessSlots
            //to find the start time for that task.
            //if it is an edge, then output it directly without any modifications.
            for (String outputLine : outputArray){
                String[] splitLine = outputLine.split("\\[");
                String taskName = splitLine[0].trim();
                String right = splitLine[1];
                int weight;

                if (!splitLine[0].contains("->")) {
                    weight = getValue(right);
                    for (ArrayList<ProcessorSlot> processor: finalSolution.getProcessors()){
                        for (ProcessorSlot processorSlot: processor){
                            if (processorSlot.getNode().getName().equals(taskName)){
                                String taskOutput = "\t"+taskName+"\t\t\t\t"+"[ Weight="+weight
                                        +",Start="+processorSlot.getStart()
                                        +",Processor="+(processorSlot.getProcessor()+1)+"];\n";
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
                System.out.println("Invalid output file name. ");
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
