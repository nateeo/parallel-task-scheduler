package graph;

/**
 * Represents weighted edge of a graph
 */
public class Edge {
    int _weight;
    Node _to;

    public Edge(Node to, int weight) {
        _weight = weight;
    }

    public Node getTo() {
        return _to;
    }

    public int getWeight() {
        return _weight;
    }
}
