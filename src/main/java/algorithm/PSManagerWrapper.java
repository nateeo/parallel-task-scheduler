package algorithm;

import graph.Graph;

public class PSManagerWrapper extends PSManager{
    public int[] _nodeVisitCounts;

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
    protected void checkAndAdd(PartialSolution ps, int processorIndex, PSPriorityQueue queue) {
        for(int i = 0; i<ps._processors.length; i++){
            for(int j=0; j<ps._processors[i].size(); j++){
                _nodeVisitCounts[ps._processors[i].get(j).getNode().getId()]++;
            }
        }
        super.checkAndAdd(ps, processorIndex, queue);


    }



}
