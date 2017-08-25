package algorithm;

import graph.Graph;

public class PSManagerWrapper extends PSManager{
    public int[] _nodeVisitCounts;
    public int _currentFinishTime;
    public int _cost;
    public int _statesExplored;
    public int _memory;

    public PSManagerWrapper(){
        super();
    }

    public PSManagerWrapper(int processors, Graph graph){
        super(processors, graph);
        _nodeVisitCounts = new int[graph.getNodes().size()+1];
    }

    public PSManagerWrapper(int processors, Graph graph, Cache cache) {
        super(processors, graph, cache);
        _nodeVisitCounts = new int[graph.getNodes().size()];
    }

    @Override
    public void generateChildren(PartialSolution parentPS, PSPriorityQueue queue) {
        _cost = parentPS._cost;
        _currentFinishTime = parentPS._latestSlot.getFinish();
        _statesExplored++;
        for(int i = 0; i<parentPS._processors.length; i++){
            for(int j=0; j<parentPS._processors[i].size(); j++){
                _nodeVisitCounts[parentPS._processors[i].get(j).getNode().getId()]++;
            }
        }
    }
}
