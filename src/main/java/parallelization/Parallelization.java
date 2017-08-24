package parallelization;//####[1]####
//####[1]####
import algorithm.Cache;//####[3]####
import algorithm.PSManager;//####[4]####
import algorithm.PSPriorityQueue;//####[5]####
import algorithm.PartialSolution;//####[6]####
import graph.Graph;//####[7]####
import java.util.ArrayList;//####[9]####
import java.util.HashMap;//####[10]####
import java.util.Map;//####[11]####
import java.util.PriorityQueue;//####[12]####
import java.util.concurrent.ExecutionException;//####[13]####
//####[13]####
//-- ParaTask related imports//####[13]####
import pt.runtime.*;//####[13]####
import java.util.concurrent.ExecutionException;//####[13]####
import java.util.concurrent.locks.*;//####[13]####
import java.lang.reflect.*;//####[13]####
import pt.runtime.GuiThread;//####[13]####
import java.util.concurrent.BlockingQueue;//####[13]####
import java.util.ArrayList;//####[13]####
import java.util.List;//####[13]####
//####[13]####
/**
 * Created by zihaoyang on 19/08/17.
 * @author sueyeonlee
 * @author zihaoyang
 *///####[19]####
public class Parallelization {//####[20]####
    static{ParaTask.init();}//####[20]####
    /*  ParaTask helper method to access private/protected slots *///####[20]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[20]####
        if (m.getParameterTypes().length == 0)//####[20]####
            m.invoke(instance);//####[20]####
        else if ((m.getParameterTypes().length == 1))//####[20]####
            m.invoke(instance, arg);//####[20]####
        else //####[20]####
            m.invoke(instance, arg, interResult);//####[20]####
    }//####[20]####
//####[22]####
    private PSPriorityQueue _parentQueue;//####[22]####
//####[23]####
    private Graph _graph;//####[23]####
//####[24]####
    private int _processors;//####[24]####
//####[25]####
    private int _cores;//####[25]####
//####[26]####
    private Cache _cache;//####[26]####
//####[27]####
    private PSPriorityQueueChild[] _childQueues;//####[27]####
//####[28]####
    private ArrayList<PartialSolution> _solutions;//####[28]####
//####[31]####
    public Parallelization(PSPriorityQueue priorityQueue, int processors, Graph graph, int cores, Cache cache) {//####[31]####
        _parentQueue = priorityQueue;//####[32]####
        _processors = processors;//####[33]####
        _graph = graph;//####[34]####
        _cores = cores;//####[35]####
        _cache = cache;//####[36]####
        _childQueues = new PSPriorityQueueChild[cores];//####[37]####
        _solutions = new ArrayList<PartialSolution>();//####[38]####
        initialiseQueues();//####[40]####
    }//####[42]####
//####[44]####
    public void initialiseQueues() {//####[44]####
        PSPriorityQueueChild[] queues = _parentQueue.splitQueue(_cores);//####[45]####
        for (int i = 0; i < _cores; i++) //####[47]####
        {//####[47]####
            _childQueues[i] = queues[i];//####[48]####
        }//####[49]####
    }//####[50]####
//####[53]####
    private static volatile Method __pt__threadQueue_PSPriorityQueueChild_PSManager_method = null;//####[53]####
    private synchronized static void __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet() {//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            try {//####[53]####
                __pt__threadQueue_PSPriorityQueueChild_PSManager_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__threadQueue", new Class[] {//####[53]####
                    PSPriorityQueueChild.class, PSManager.class//####[53]####
                });//####[53]####
            } catch (Exception e) {//####[53]####
                e.printStackTrace();//####[53]####
            }//####[53]####
        }//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, PSManager psManager, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setParameters(childQueue, psManager);//####[53]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, PSManager psManager) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, PSManager psManager, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setTaskIdArgIndexes(0);//####[53]####
        taskinfo.addDependsOn(childQueue);//####[53]####
        taskinfo.setParameters(childQueue, psManager);//####[53]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, PSManager psManager) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, PSManager psManager, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setQueueArgIndexes(0);//####[53]####
        taskinfo.setIsPipeline(true);//####[53]####
        taskinfo.setParameters(childQueue, psManager);//####[53]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskID<PSManager> psManager) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setTaskIdArgIndexes(1);//####[53]####
        taskinfo.addDependsOn(psManager);//####[53]####
        taskinfo.setParameters(childQueue, psManager);//####[53]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[53]####
        taskinfo.addDependsOn(childQueue);//####[53]####
        taskinfo.addDependsOn(psManager);//####[53]####
        taskinfo.setParameters(childQueue, psManager);//####[53]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setQueueArgIndexes(0);//####[53]####
        taskinfo.setIsPipeline(true);//####[53]####
        taskinfo.setTaskIdArgIndexes(1);//####[53]####
        taskinfo.addDependsOn(psManager);//####[53]####
        taskinfo.setParameters(childQueue, psManager);//####[53]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, BlockingQueue<PSManager> psManager) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setQueueArgIndexes(1);//####[53]####
        taskinfo.setIsPipeline(true);//####[53]####
        taskinfo.setParameters(childQueue, psManager);//####[53]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setQueueArgIndexes(1);//####[53]####
        taskinfo.setIsPipeline(true);//####[53]####
        taskinfo.setTaskIdArgIndexes(0);//####[53]####
        taskinfo.addDependsOn(childQueue);//####[53]####
        taskinfo.setParameters(childQueue, psManager);//####[53]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager) {//####[53]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[53]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[53]####
    }//####[53]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[53]####
        // ensure Method variable is set//####[53]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[53]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[53]####
        }//####[53]####
        taskinfo.setQueueArgIndexes(0, 1);//####[53]####
        taskinfo.setIsPipeline(true);//####[53]####
        taskinfo.setParameters(childQueue, psManager);//####[53]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[53]####
        taskinfo.setInstance(this);//####[53]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[53]####
    }//####[53]####
    public void __pt__threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {//####[53]####
        PartialSolution ps = null;//####[54]####
        while (childQueue.hasNext()) //####[55]####
        {//####[55]####
            ps = childQueue.getCurrentPartialSolution();//####[56]####
            psManager.generateChildren(ps, childQueue);//####[57]####
        }//####[58]####
        ps = childQueue.getCurrentPartialSolution();//####[59]####
        _solutions.add(ps);//####[60]####
    }//####[61]####
//####[61]####
//####[63]####
    public PartialSolution findOptimal() throws ExecutionException, InterruptedException {//####[63]####
        PartialSolution[] ps = new PartialSolution[_cores];//####[64]####
        TaskIDGroup g = new TaskIDGroup(_cores);//####[65]####
        for (int i = 0; i < _cores; i++) //####[66]####
        {//####[66]####
            PSManager psManager = new PSManager(_processors, _graph, _cache);//####[67]####
            TaskID id = threadQueue(_childQueues[i], psManager);//####[68]####
            g.add(id);//####[69]####
        }//####[70]####
        g.waitTillFinished();//####[71]####
        for (PartialSolution p : _solutions) //####[72]####
        {//####[72]####
        }//####[73]####
        PartialSolution solution = null;//####[74]####
        int finalTime = -1;//####[75]####
        for (int i = 0; i < _solutions.size(); i++) //####[76]####
        {//####[76]####
            int psFinishTime = _solutions.get(i)._latestSlot.getFinish();//####[77]####
            if (finalTime == -1 || psFinishTime < finalTime) //####[78]####
            {//####[78]####
                solution = _solutions.get(i);//####[79]####
                finalTime = psFinishTime;//####[80]####
            }//####[81]####
        }//####[82]####
        return solution;//####[83]####
    }//####[84]####
}//####[84]####
