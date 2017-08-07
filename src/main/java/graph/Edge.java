package graph;

/**
 * Represents weighted edge of a graph
 */
public class Edge {
    int _weight;

    Node _to;
    Node _from;

    public Edge(Node from, Node to, int weight) {
        _from = from;
        _to = to;
        _weight = weight;
    }

    public Node getTo() {
        return _to;
    }

    public Node getFrom() {
        return _from;
    }

    public int getWeight() {
        return _weight;
    }

    @Override
    public String toString() {
        return _from._name + _to._name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Edge) {
            return this.toString().equals(other.toString());
        }
        return false;
    }
}
