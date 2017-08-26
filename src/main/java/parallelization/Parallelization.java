package parallelization;//####[1]####
//####[1]####
import javafx.application.Application;//####[3]####
import frontend.Main;//####[4]####
import algorithm.Cache;//####[5]####
import algorithm.PSManager;//####[6]####
import algorithm.PSManagerGroup;//####[7]####
import algorithm.PSManagerWrapper;//####[8]####
import algorithm.PSPriorityQueue;//####[9]####
import algorithm.PartialSolution;//####[10]####
import frontend.Listener;//####[11]####
import graph.Graph;//####[12]####
import pt.runtime.TaskID;//####[13]####
import pt.runtime.TaskIDGroup;//####[14]####
import scheduler.Scheduler;//####[15]####
import java.util.*;//####[17]####
import java.util.concurrent.ExecutionException;//####[18]####
//####[18]####
//-- ParaTask related imports//####[18]####
import pt.runtime.*;//####[18]####
import java.util.concurrent.ExecutionException;//####[18]####
import java.util.concurrent.locks.*;//####[18]####
import java.lang.reflect.*;//####[18]####
import pt.runtime.GuiThread;//####[18]####
import java.util.concurrent.BlockingQueue;//####[18]####
import java.util.ArrayList;//####[18]####
import java.util.List;//####[18]####
//####[18]####
/**
 * Created by zihaoyang on 19/08/17.
 * @author sueyeonlee
 * @author zihaoyang
 *///####[24]####
public class Parallelization {//####[25]####
    static{ParaTask.init();}//####[25]####
    /*  ParaTask helper method to access private/protected slots *///####[25]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[25]####
        if (m.getParameterTypes().length == 0)//####[25]####
            m.invoke(instance);//####[25]####
        else if ((m.getParameterTypes().length == 1))//####[25]####
            m.invoke(instance, arg);//####[25]####
        else //####[25]####
            m.invoke(instance, arg, interResult);//####[25]####
    }//####[25]####
//####[27]####
    private PSPriorityQueue _parentQueue;//####[27]####
//####[28]####
    private Graph _graph;//####[28]####
//####[29]####
    private int _processors;//####[29]####
//####[30]####
    private int _cores;//####[30]####
//####[31]####
    private Cache _cache;//####[31]####
//####[32]####
    private PSPriorityQueueChild[] _childQueues;//####[32]####
//####[33]####
    private ArrayList<PartialSolution> _solutions;//####[33]####
//####[36]####
    public Parallelization(PSPriorityQueue priorityQueue, int processors, Graph graph, int cores, Cache cache) {//####[36]####
        _parentQueue = priorityQueue;//####[37]####
        _processors = processors;//####[38]####
        _graph = graph;//####[39]####
        _cores = cores;//####[40]####
        _cache = cache;//####[41]####
        _childQueues = new PSPriorityQueueChild[cores];//####[42]####
        _solutions = new ArrayList<PartialSolution>();//####[43]####
        initialiseQueues();//####[45]####
    }//####[47]####
//####[49]####
    public void initialiseQueues() {//####[49]####
        PSPriorityQueueChild[] queues = _parentQueue.splitQueue(_cores);//####[50]####
        for (int i = 0; i < _cores; i++) //####[52]####
        {//####[52]####
            _childQueues[i] = queues[i];//####[53]####
        }//####[54]####
    }//####[55]####
//####[58]####
    private static volatile Method __pt__threadQueue_PSPriorityQueueChild_PSManager_method = null;//####[58]####
    private synchronized static void __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet() {//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            try {//####[58]####
                __pt__threadQueue_PSPriorityQueueChild_PSManager_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__threadQueue", new Class[] {//####[58]####
                    PSPriorityQueueChild.class, PSManager.class//####[58]####
                });//####[58]####
            } catch (Exception e) {//####[58]####
                e.printStackTrace();//####[58]####
            }//####[58]####
        }//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, PSManager psManager, TaskInfo taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setParameters(childQueue, psManager);//####[58]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[58]####
        taskinfo.setInstance(this);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, PSManager psManager) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, PSManager psManager, TaskInfo taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setTaskIdArgIndexes(0);//####[58]####
        taskinfo.addDependsOn(childQueue);//####[58]####
        taskinfo.setParameters(childQueue, psManager);//####[58]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[58]####
        taskinfo.setInstance(this);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, PSManager psManager) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, PSManager psManager, TaskInfo taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setQueueArgIndexes(0);//####[58]####
        taskinfo.setIsPipeline(true);//####[58]####
        taskinfo.setParameters(childQueue, psManager);//####[58]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[58]####
        taskinfo.setInstance(this);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskID<PSManager> psManager) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setTaskIdArgIndexes(1);//####[58]####
        taskinfo.addDependsOn(psManager);//####[58]####
        taskinfo.setParameters(childQueue, psManager);//####[58]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[58]####
        taskinfo.setInstance(this);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[58]####
        taskinfo.addDependsOn(childQueue);//####[58]####
        taskinfo.addDependsOn(psManager);//####[58]####
        taskinfo.setParameters(childQueue, psManager);//####[58]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[58]####
        taskinfo.setInstance(this);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setQueueArgIndexes(0);//####[58]####
        taskinfo.setIsPipeline(true);//####[58]####
        taskinfo.setTaskIdArgIndexes(1);//####[58]####
        taskinfo.addDependsOn(psManager);//####[58]####
        taskinfo.setParameters(childQueue, psManager);//####[58]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[58]####
        taskinfo.setInstance(this);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, BlockingQueue<PSManager> psManager) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setQueueArgIndexes(1);//####[58]####
        taskinfo.setIsPipeline(true);//####[58]####
        taskinfo.setParameters(childQueue, psManager);//####[58]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[58]####
        taskinfo.setInstance(this);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setQueueArgIndexes(1);//####[58]####
        taskinfo.setIsPipeline(true);//####[58]####
        taskinfo.setTaskIdArgIndexes(0);//####[58]####
        taskinfo.addDependsOn(childQueue);//####[58]####
        taskinfo.setParameters(childQueue, psManager);//####[58]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[58]####
        taskinfo.setInstance(this);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[58]####
    }//####[58]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[58]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setQueueArgIndexes(0, 1);//####[58]####
        taskinfo.setIsPipeline(true);//####[58]####
        taskinfo.setParameters(childQueue, psManager);//####[58]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[58]####
        taskinfo.setInstance(this);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    public void __pt__threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {//####[58]####
        PartialSolution ps = null;//####[59]####
        while (childQueue.hasNext()) //####[60]####
        {//####[60]####
            ps = childQueue.getCurrentPartialSolution();//####[61]####
            psManager.generateChildren(ps, childQueue);//####[62]####
        }//####[63]####
        ps = childQueue.getCurrentPartialSolution();//####[64]####
        _solutions.add(ps);//####[65]####
    }//####[66]####
//####[66]####
//####[68]####
    public PartialSolution findOptimal(PSManagerGroup group) throws ExecutionException, InterruptedException {//####[68]####
        PartialSolution[] ps = new PartialSolution[_cores];//####[69]####
        TaskIDGroup g = new TaskIDGroup(_cores);//####[70]####
        for (int i = 0; i < _cores; i++) //####[71]####
        {//####[71]####
            PSManager psManager = new PSManagerWrapper(_processors, _graph, _cache, i);//####[72]####
            if (group != null) //####[73]####
            group.add(psManager);//####[73]####
            TaskID id = threadQueue(_childQueues[i], psManager);//####[74]####
            g.add(id);//####[75]####
        }//####[76]####
        g.waitTillFinished();//####[77]####
        for (PartialSolution p : _solutions) //####[78]####
        {//####[78]####
        }//####[79]####
        PartialSolution solution = null;//####[80]####
        int finalTime = -1;//####[81]####
        for (int i = 0; i < _solutions.size(); i++) //####[82]####
        {//####[82]####
            int psFinishTime = _solutions.get(i)._latestSlot.getFinish();//####[83]####
            if (finalTime == -1 || psFinishTime < finalTime) //####[84]####
            {//####[84]####
                solution = _solutions.get(i);//####[85]####
                finalTime = psFinishTime;//####[86]####
            }//####[87]####
        }//####[88]####
        return solution;//####[89]####
    }//####[90]####
}//####[90]####
