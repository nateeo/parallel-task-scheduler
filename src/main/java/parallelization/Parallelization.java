package parallelization;//####[1]####
//####[1]####
import algorithm.PSManager;//####[3]####
import algorithm.PSPriorityQueue;//####[4]####
import algorithm.Cache;//####[5]####
import algorithm.PartialSolution;//####[6]####
import graph.Graph;//####[7]####
import pt.runtime.TaskID;//####[8]####
import pt.runtime.TaskIDGroup;//####[9]####
import java.util.ArrayList;//####[11]####
import java.util.concurrent.ExecutionException;//####[12]####
//####[12]####
//-- ParaTask related imports//####[12]####
import pt.runtime.*;//####[12]####
import java.util.concurrent.ExecutionException;//####[12]####
import java.util.concurrent.locks.*;//####[12]####
import java.lang.reflect.*;//####[12]####
import pt.runtime.GuiThread;//####[12]####
import java.util.concurrent.BlockingQueue;//####[12]####
import java.util.ArrayList;//####[12]####
import java.util.List;//####[12]####
//####[12]####
/**
 * Created by zihaoyang on 19/08/17.
 * @author sueyeonlee
 * @author zihaoyang
 *///####[18]####
public class Parallelization {//####[19]####
    static{ParaTask.init();}//####[19]####
    /*  ParaTask helper method to access private/protected slots *///####[19]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[19]####
        if (m.getParameterTypes().length == 0)//####[19]####
            m.invoke(instance);//####[19]####
        else if ((m.getParameterTypes().length == 1))//####[19]####
            m.invoke(instance, arg);//####[19]####
        else //####[19]####
            m.invoke(instance, arg, interResult);//####[19]####
    }//####[19]####
//####[21]####
    private PSPriorityQueue _parentQueue;//####[21]####
//####[22]####
    private Cache _cache;//####[22]####
//####[23]####
    private Graph _graph;//####[23]####
//####[24]####
    private int _processors;//####[24]####
//####[25]####
    private int _cores;//####[25]####
//####[26]####
    private PSPriorityQueueChild[] _childQueues;//####[26]####
//####[27]####
    private ArrayList<PartialSolution> _solutions;//####[27]####
//####[28]####
    private PartialSolution _preOptimalSolution;//####[28]####
//####[29]####
    private int _fastestSolution;//####[29]####
//####[33]####
    public Parallelization(PSPriorityQueue priorityQueue, int processors, Graph graph, int cores, Cache cache) {//####[33]####
        _parentQueue = priorityQueue;//####[34]####
        _processors = processors;//####[35]####
        _graph = graph;//####[36]####
        _cores = cores;//####[37]####
        _childQueues = new PSPriorityQueueChild[cores];//####[38]####
        _solutions = new ArrayList<PartialSolution>();//####[39]####
        _cache = cache;//####[40]####
        _preOptimalSolution = null;//####[41]####
        initialiseQueues();//####[44]####
    }//####[46]####
//####[48]####
    public void initialiseQueues() {//####[48]####
        PSPriorityQueueChild[] queues = _parentQueue.splitQueue(_cores);//####[49]####
        for (int i = 0; i < _cores; i++) //####[51]####
        {//####[51]####
            _childQueues[i] = queues[i];//####[52]####
        }//####[53]####
    }//####[54]####
//####[56]####
    private static volatile Method __pt__threadQueue_PSPriorityQueueChild_PSManager_method = null;//####[56]####
    private synchronized static void __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet() {//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            try {//####[56]####
                __pt__threadQueue_PSPriorityQueueChild_PSManager_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__threadQueue", new Class[] {//####[56]####
                    PSPriorityQueueChild.class, PSManager.class//####[56]####
                });//####[56]####
            } catch (Exception e) {//####[56]####
                e.printStackTrace();//####[56]####
            }//####[56]####
        }//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(PSPriorityQueueChild childQueue, PSManager psManager, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setParameters(childQueue, psManager);//####[56]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(TaskID<PSPriorityQueueChild> childQueue, PSManager psManager) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(TaskID<PSPriorityQueueChild> childQueue, PSManager psManager, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setTaskIdArgIndexes(0);//####[56]####
        taskinfo.addDependsOn(childQueue);//####[56]####
        taskinfo.setParameters(childQueue, psManager);//####[56]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, PSManager psManager) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, PSManager psManager, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setQueueArgIndexes(0);//####[56]####
        taskinfo.setIsPipeline(true);//####[56]####
        taskinfo.setParameters(childQueue, psManager);//####[56]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(PSPriorityQueueChild childQueue, TaskID<PSManager> psManager) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(PSPriorityQueueChild childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setTaskIdArgIndexes(1);//####[56]####
        taskinfo.addDependsOn(psManager);//####[56]####
        taskinfo.setParameters(childQueue, psManager);//####[56]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[56]####
        taskinfo.addDependsOn(childQueue);//####[56]####
        taskinfo.addDependsOn(psManager);//####[56]####
        taskinfo.setParameters(childQueue, psManager);//####[56]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setQueueArgIndexes(0);//####[56]####
        taskinfo.setIsPipeline(true);//####[56]####
        taskinfo.setTaskIdArgIndexes(1);//####[56]####
        taskinfo.addDependsOn(psManager);//####[56]####
        taskinfo.setParameters(childQueue, psManager);//####[56]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(PSPriorityQueueChild childQueue, BlockingQueue<PSManager> psManager) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(PSPriorityQueueChild childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setQueueArgIndexes(1);//####[56]####
        taskinfo.setIsPipeline(true);//####[56]####
        taskinfo.setParameters(childQueue, psManager);//####[56]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(TaskID<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(TaskID<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setQueueArgIndexes(1);//####[56]####
        taskinfo.setIsPipeline(true);//####[56]####
        taskinfo.setTaskIdArgIndexes(0);//####[56]####
        taskinfo.addDependsOn(childQueue);//####[56]####
        taskinfo.setParameters(childQueue, psManager);//####[56]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager) {//####[56]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[56]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[56]####
    }//####[56]####
    private TaskID<PartialSolution> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[56]####
        // ensure Method variable is set//####[56]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[56]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[56]####
        }//####[56]####
        taskinfo.setQueueArgIndexes(0, 1);//####[56]####
        taskinfo.setIsPipeline(true);//####[56]####
        taskinfo.setParameters(childQueue, psManager);//####[56]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[56]####
        taskinfo.setInstance(this);//####[56]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[56]####
    }//####[56]####
    public PartialSolution __pt__threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {//####[56]####
        PartialSolution ps = null;//####[57]####
        while (childQueue.hasNext()) //####[58]####
        {//####[58]####
            ps = childQueue.getCurrentPartialSolution();//####[59]####
            if (_preOptimalSolution == null || ps._latestSlot.getFinish() < _fastestSolution) //####[60]####
            {//####[60]####
                psManager.generateChildren(ps, childQueue);//####[61]####
            }//####[62]####
            System.out.println(childQueue.size());//####[63]####
        }//####[64]####
        ps = childQueue.getCurrentPartialSolution();//####[65]####
        System.out.println("8====D adding\n" + ps);//####[66]####
        return ps;//####[67]####
    }//####[68]####
//####[68]####
//####[70]####
    public PartialSolution findOptimal() throws ExecutionException, InterruptedException {//####[70]####
        PartialSolution[] ps = new PartialSolution[_cores];//####[71]####
        TaskIDGroup g = new TaskIDGroup(_cores);//####[72]####
        for (int i = 0; i < _cores; i++) //####[73]####
        {//####[73]####
            PSManager psManager = new PSManager(_processors, _graph, _cache);//####[74]####
            TaskID<PartialSolution> id = threadQueue(_childQueues[i], psManager);//####[75]####
            PartialSolution partialSolution = id.getReturnResult();//####[76]####
            if (_preOptimalSolution == null || partialSolution._latestSlot.getFinish() < _preOptimalSolution._latestSlot.getFinish()) //####[77]####
            {//####[77]####
                _preOptimalSolution = partialSolution;//####[78]####
                _fastestSolution = partialSolution._latestSlot.getFinish();//####[79]####
                System.out.println("NEW BEST!");//####[80]####
            }//####[81]####
            g.add(id);//####[82]####
        }//####[84]####
        g.waitTillFinished();//####[85]####
        return _preOptimalSolution;//####[86]####
    }//####[87]####
//####[89]####
    public void printSolutions() {//####[89]####
        System.out.println("**************ZIHAO'S OUPUT********************");//####[91]####
        for (PartialSolution solution : _solutions) //####[93]####
        {//####[93]####
            System.out.println(solution.toString());//####[94]####
        }//####[95]####
        System.out.println("**************ZIHAO'S OUPUT********************");//####[97]####
    }//####[98]####
//####[100]####
    public void printChildQueues() {//####[100]####
        for (int i = 0; i < _childQueues.length; i++) //####[101]####
        {//####[101]####
            System.out.println("queue " + i);//####[103]####
            PSPriorityQueueChild queue = _childQueues[i];//####[105]####
            queue.printQueue();//####[106]####
        }//####[107]####
    }//####[108]####
}//####[108]####
