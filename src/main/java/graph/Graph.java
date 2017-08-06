package graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a graph and contains some metadata
 */
public class Graph {
    String _name;
    ArrayList<Node> _start;
    ArrayList<Node> _nodes;
    int _totalMinimumWork;

    public Graph(String name) {
        _name = name;
    }

    public void setStart(ArrayList<Node> start) {
        _start = start;
    }

    public void setNodes(ArrayList<Node> nodes){
        _nodes = nodes;
    }

    public void setTotalMinimumWork(int work) {
        _totalMinimumWork = work;
    }

    public ArrayList<Node> getStart() {
        return _start;
    }

    public List<Node> getNodes() { return _nodes; }

    public String getName() {
        return _name;
    }

    public int totalMinimumWork() {
        return _totalMinimumWork;
    }

    // TODO
    @Override
    public String toString() {
        String nodes = "";
        return nodes;
    }

}
