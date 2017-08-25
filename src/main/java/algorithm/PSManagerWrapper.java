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
                // update currentFinishTime, cost, loaded and states explored
                _currentStatPS = ps;
                _loaded = (double)ps._nodes.size() / (_totalNodes);
                _currentFinishTime = ps._latestSlot.getFinish();
                _cost = ps._cost;
                _memory++;
                // update _nodeVisitCounts
                queue.add(ps);
            }
        }
    }

    @Override
    public void generateChildren(PartialSolution ps, PSPriorityQueue queue) {
        for(int i = 0; i<ps._processors.length; i++){
            for(int j=0; j<ps._processors[i].size(); j++){
                _nodeVisitCounts[ps._processors[i].get(j).getNode().getId() - 1]++;
            }
        }
        _statesExplored++;
        super.generateChildren(ps, queue);
    }



}
