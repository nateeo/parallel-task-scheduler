package graph;


import java.util.ArrayList;

/**
 * Class to represent the weighted node
 */
public class Node {
    String _name;
    int _id;
    int _topId; // topological id
    int _weight;
    ArrayList<Edge> _outgoing;
    ArrayList<Edge> _incoming;
    ArrayList<Node> _parents;

    public Node(int id, String name, int weight) {
        _id = id;
        _name = name;
        _weight = weight;
        _outgoing = new ArrayList<Edge>();
        _incoming = new ArrayList<Edge>();
        _parents = new ArrayList<Node>();
    }

    public void addOutgoingEdge(Node to, int weight) {
        _outgoing.add(new Edge(this, to, weight));
    }

    public void addIncomingEdge(Node from, int weight) {
        _incoming.add(new Edge(from, this, weight));
        _parents.add(from);
    }

    public ArrayList<Edge> getOutgoing() {
        return _outgoing;
    }

    public ArrayList<Edge> getIncoming() {
        return _incoming;
    }

    public ArrayList<Node> getParentNodes() {
        return _parents;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public void setTopId(int id) {
        _topId = id;
    }

    public int getTopId() {
        return _topId;
    }

    public String getName() {
        return _name;
    }

    public int getWeight() {
        return _weight;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Node) {
            return this._name.equals(((Node)other)._name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this._id;
    }

    @Override
    public String toString() {
        return _name;
    }
}