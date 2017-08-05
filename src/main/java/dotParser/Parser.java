package dotParser;

import graph.Graph;
import graph.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * THis class contains methods to:
 * parse .dot files to a JGraph data structure
 * generate .dot files from a JGraph data structure
 */
public class Parser {

    /**
     * Parses .dot file and returns a JGraph representation.
     * @param file
     * @return Graph, null if there was an error reading the file
     * @throws IOException
     */
    public static Graph parseDotFile (File file) {
        HashMap<String, Node> nodeMap = new HashMap<String, Node>();
        Graph graph;
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

                if (splitLine[0].trim().length() == 1) { // add single vertex to graph and hashmap
                    weight = getValue(right);
                    Node newVertex = new Node(left, weight);
                    nodeMap.put(left, newVertex);
                } else { // add arc to queue for processing at the end
                    arcQueue.add(new String[]{left, right});
                }
            }
        } catch (IOException e) {
            return null;
        }
        // process arcQueue
        for (String[] string : arcQueue) {
            String[] arcString = string[0].split("->");
            String from = arcString[1];
            String to =  arcString[0];
            int weight = getValue(string[1]);
            nodeMap.get(from).addEdge(nodeMap.get(to), weight);
        }

        return graph;
    }

    /**
     * Outputs a graph to a .dot file
     * @param name name of output file
     */
    public static void outputGraphToFile(String name) {

    }

    private static int getValue(String value) {
        return Integer.parseInt(value.substring(value.indexOf("=") + 1, value.lastIndexOf("]")).trim());
    }
}
