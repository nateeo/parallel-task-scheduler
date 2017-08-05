package graph;

import java.util.ArrayList;

/**
 * Class that represents a graph and contains some metadata
 */
public class Graph {
    String _name;
    ArrayList<Node> _start;

    public Graph(String name) {
        _name = name;
    }

    public void setStart(ArrayList<Node> start) {
        _start = start;
    }

    public ArrayList<Node> getStart() {
        return _start;
    }

    public String getName() {
        return _name;
    }
}
