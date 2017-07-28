package graph;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * Created by nateeo on 28/07/17.
 */
public class WeightedDigraph extends SimpleDirectedWeightedGraph<WeightedVertex, DefaultWeightedEdge> {
    public WeightedDigraph() {
        super(DefaultWeightedEdge.class);
    }

    @Override
    public void setEdgeWeight(DefaultWeightedEdge e, double weight) {
        super.setEdgeWeight(e, weight);
    }
}
