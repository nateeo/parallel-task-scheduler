package algorithm;

import graph.Graph;

public class PSManagerWrapper extends PSManager{

    protected int _totalNodes;

    public PSManagerWrapper(){
        super();
    }

    public PSManagerWrapper(int processors, Graph graph){
        super(processors, graph);
        _nodeVisitCounts = new int[graph.getNodes().size()];
        _totalNodes = _graph.getNodes().size();
    }

    public PSManagerWrapper(int processors, Graph graph, Cache cache) {
        super(processors, graph, cache);
        _nodeVisitCounts = new int[graph.getNodes().size()];
    }

    @Override
    protected void checkAndAdd(PartialSolution ps, int processorIndex, PSPriorityQueue queue) {
        if (!equivalenceCheck(ps, processorIndex)) {
            if (_cache.add(ps)) {
                _memory++;
                queue.add(ps);
            }
        }
    }

    @Override
    public void generateChildren(PartialSolution ps, PSPriorityQueue queue) {

        // update currentFinishTime, cost, loaded and states explored
        _currentFinishTime = ps._currentFinishTime;
        _cost = ps._cost;
        _statesExplored++;
        _loaded = ps._nodes.size() / _totalNodes;

        // update _nodeVisitCounts
        for(int i = 0; i<ps._processors.length; i++){
            for(int j=0; j<ps._processors[i].size(); j++){
                _nodeVisitCounts[ps._processors[i].get(j).getNode().getId() - 1]++;
            }
        }
        super.generateChildren(ps, queue);
    }



}
