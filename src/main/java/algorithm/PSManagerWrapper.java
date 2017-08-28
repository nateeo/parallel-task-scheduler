package algorithm;

import graph.Graph;

/**
 * Wrapper class for algorithm.PSManager
 * PSManagerWrapper is generated when the schedule is called with the -v flag for visualisation
 * PSManagerWrapper is needed to stash important statistics for the frontend
 */
public class PSManagerWrapper extends PSManager{

    /**
     * Calls the parent constructor
     */
    public PSManagerWrapper(){
        super();
    }

    /**
     * Calls the parent constructor and initialises the _nodeVisitCounts field to track the number
     * of times each node in the graph is visited
     * @param processors
     * @param graph
     */
    public PSManagerWrapper(int processors, Graph graph){
        super(processors, graph);
        _nodeVisitCounts = new int[graph.getNodes().size()];
    }

    /**
     * Call the parent constructor algorithm.PSManagerWrapper()
     * @param processors
     * @param graph
     * @param cache
     * @param id
     */
    public PSManagerWrapper(int processors, Graph graph, Cache cache, int id) {
        super(processors, graph, cache, id);
    }

    /**
     * overrides the parent method checkAndAdd() to update the stat based fields
     * to be passed to the front end
     * @param ps
     * @param processorIndex
     * @param queue
     */
    @Override
    protected void checkAndAdd(PartialSolution ps, int processorIndex, PSPriorityQueue queue) {
        if (!equivalenceCheck(ps, processorIndex)) {
            if (_cache.add(ps)) {
                // update currentFinishTime, cost, loaded and states explored
                _currentStatPS = ps;
                _loaded = (double)ps._nodes.size() / (_graph.getNodes().size());
                _currentFinishTime = ps._latestSlot.getFinish();
                _cost = ps._cost;
                _memory++;
                // update _nodeVisitCounts
                queue.add(ps);
            }
        }
    }

    /**
     * overrides the parent method so that the number of states explored can be updated
     * @param ps
     * @param queue the queue to add the children to
     */
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
