package graph;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * decorator for SimpleDirectedGraph adding functionality to set edge weight externally, as well as accessing a name
 * Created by nateeo on 28/07/17.
 */
public class WeightedDigraph extends SimpleDirectedWeightedGraph<WeightedVertex, DefaultWeightedEdge> {
    String _name = "";

    public WeightedDigraph() {
        super(DefaultWeightedEdge.class);
    }

    public WeightedDigraph(String name) {
        this();
        _name = name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public double getEdgeWeight(DefaultWeightedEdge e) {
        return super.getEdgeWeight(e);
    }

    @Override
    public void setEdgeWeight(DefaultWeightedEdge e, double weight) {
        super.setEdgeWeight(e, weight);
    }
}
