package graph;

/**
 * Class that represents a graph and contains some metadata
 */
public class Graph {
    String _name;
    Node _start;

    public Graph(String name) {
        _name = name;
    }

    public void setStart(Node start) {
        _start = start;
    }

    public Node getStart() {
        return _start;
    }

    public String getName() {
        return _name;
    }
}
