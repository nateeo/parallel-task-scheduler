package parallelization;//####[1]####
//####[1]####
import algorithm.PSManager;//####[3]####
import algorithm.PSPriorityQueue;//####[4]####
import algorithm.PartialSolution;//####[5]####
import graph.Graph;//####[6]####
import java.util.HashMap;//####[8]####
import java.util.Map;//####[9]####
import java.util.PriorityQueue;//####[10]####
//####[10]####
//-- ParaTask related imports//####[10]####
import pt.runtime.*;//####[10]####
import java.util.concurrent.ExecutionException;//####[10]####
import java.util.concurrent.locks.*;//####[10]####
import java.lang.reflect.*;//####[10]####
import pt.runtime.GuiThread;//####[10]####
import java.util.concurrent.BlockingQueue;//####[10]####
import java.util.ArrayList;//####[10]####
import java.util.List;//####[10]####
//####[10]####
/**
 * Created by zihaoyang on 19/08/17.
 * @author sueyeonlee
 * @author zihaoyang
 *///####[16]####
public class Parallelization {//####[17]####
    static{ParaTask.init();}//####[17]####
    /*  ParaTask helper method to access private/protected slots *///####[17]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[17]####
        if (m.getParameterTypes().length == 0)//####[17]####
            m.invoke(instance);//####[17]####
        else if ((m.getParameterTypes().length == 1))//####[17]####
            m.invoke(instance, arg);//####[17]####
        else //####[17]####
            m.invoke(instance, arg, interResult);//####[17]####
    }//####[17]####
//####[19]####
    private PSPriorityQueue _parentQueue;//####[19]####
//####[20]####
    private PSManager _psManager;//####[20]####
//####[21]####
    private Graph _graph;//####[21]####
//####[22]####
    private int _processors;//####[22]####
//####[23]####
    private int _cores;//####[23]####
//####[24]####
    private PSPriorityQueueChild[] _childQueues;//####[24]####
//####[25]####
    private PartialSolution _solution;//####[25]####
//####[26]####
    private double _finalTime;//####[26]####
//####[29]####
    public Parallelization(PSPriorityQueue priorityQueue, PSManager psManager, int cores) {//####[29]####
        _parentQueue = priorityQueue;//####[30]####
        _psManager = psManager;//####[31]####
        _cores = cores;//####[32]####
        _childQueues = new PSPriorityQueueChild[cores];//####[33]####
        _solution = null;//####[34]####
        _finalTime = Double.POSITIVE_INFINITY;//####[35]####
        initialiseQueues();//####[37]####
    }//####[39]####
//####[41]####
    public void initialiseQueues() {//####[41]####
        PSPriorityQueueChild[] queues = _parentQueue.splitQueue(_cores);//####[42]####
        for (int i = 0; i < _cores; i++) //####[44]####
        {//####[44]####
            _childQueues[i] = queues[i];//####[45]####
        }//####[46]####
    }//####[47]####
//####[49]####
    private static volatile Method __pt__threadQueue_PSPriorityQueueChild_method = null;//####[49]####
    private synchronized static void __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet() {//####[49]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[49]####
            try {//####[49]####
                __pt__threadQueue_PSPriorityQueueChild_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__threadQueue", new Class[] {//####[49]####
                    PSPriorityQueueChild.class//####[49]####
                });//####[49]####
            } catch (Exception e) {//####[49]####
                e.printStackTrace();//####[49]####
            }//####[49]####
        }//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue) {//####[49]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[49]####
        return threadQueue(childQueue, new TaskInfo());//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskInfo taskinfo) {//####[49]####
        // ensure Method variable is set//####[49]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[49]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[49]####
        }//####[49]####
        taskinfo.setParameters(childQueue);//####[49]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[49]####
        taskinfo.setInstance(this);//####[49]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue) {//####[49]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[49]####
        return threadQueue(childQueue, new TaskInfo());//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskInfo taskinfo) {//####[49]####
        // ensure Method variable is set//####[49]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[49]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[49]####
        }//####[49]####
        taskinfo.setTaskIdArgIndexes(0);//####[49]####
        taskinfo.addDependsOn(childQueue);//####[49]####
        taskinfo.setParameters(childQueue);//####[49]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[49]####
        taskinfo.setInstance(this);//####[49]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue) {//####[49]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[49]####
        return threadQueue(childQueue, new TaskInfo());//####[49]####
    }//####[49]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskInfo taskinfo) {//####[49]####
        // ensure Method variable is set//####[49]####
        if (__pt__threadQueue_PSPriorityQueueChild_method == null) {//####[49]####
            __pt__threadQueue_PSPriorityQueueChild_ensureMethodVarSet();//####[49]####
        }//####[49]####
        taskinfo.setQueueArgIndexes(0);//####[49]####
        taskinfo.setIsPipeline(true);//####[49]####
        taskinfo.setParameters(childQueue);//####[49]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_method);//####[49]####
        taskinfo.setInstance(this);//####[49]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[49]####
    }//####[49]####
    public void __pt__threadQueue(PSPriorityQueueChild childQueue) {//####[49]####
        PartialSolution ps = null;//####[50]####
        while (childQueue.hasNext()) //####[51]####
        {//####[51]####
            ps = childQueue.getCurrentPartialSolution();//####[52]####
            _psManager.generateChildren(ps, childQueue);//####[53]####
        }//####[54]####
        ps = childQueue.getCurrentPartialSolution();//####[55]####
        if (ps._currentFinishTime < _finalTime) //####[56]####
        {//####[56]####
            _solution = ps;//####[57]####
            _finalTime = ps.getCurrentFinshTime();//####[58]####
        }//####[59]####
    }//####[60]####
//####[60]####
//####[62]####
    public PartialSolution findOptimal() {//####[62]####
        PartialSolution[] ps = new PartialSolution[_cores];//####[63]####
        for (int i = 0; i < _cores; i++) //####[64]####
        {//####[64]####
            threadQueue(_childQueues[i]);//####[65]####
        }//####[66]####
        return _solution;//####[67]####
    }//####[68]####
}//####[68]####
