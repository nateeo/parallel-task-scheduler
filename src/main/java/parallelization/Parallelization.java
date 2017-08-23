package parallelization;//####[1]####
//####[1]####
import algorithm.PSManager;//####[3]####
import algorithm.PSPriorityQueue;//####[4]####
import algorithm.Cache;//####[5]####
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
    private Cache _cache;//####[23]####
//####[24]####
    private Graph _graph;//####[24]####
//####[25]####
    private int _processors;//####[25]####
//####[26]####
    private int _cores;//####[26]####
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
        _childQueues = new PSPriorityQueueChild[cores];//####[36]####
        _solutions = new ArrayList<PartialSolution>();//####[37]####
        _cache = cache;//####[38]####
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
//####[52]####
    private static volatile Method __pt__threadQueue_PSPriorityQueueChild_PSManager_method = null;//####[52]####
    private synchronized static void __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet() {//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            try {//####[52]####
                __pt__threadQueue_PSPriorityQueueChild_PSManager_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__threadQueue", new Class[] {//####[52]####
                    PSPriorityQueueChild.class, PSManager.class//####[52]####
                });//####[52]####
            } catch (Exception e) {//####[52]####
                e.printStackTrace();//####[52]####
            }//####[52]####
        }//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, PSManager psManager, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setParameters(childQueue, psManager);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, PSManager psManager) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, PSManager psManager, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setTaskIdArgIndexes(0);//####[52]####
        taskinfo.addDependsOn(childQueue);//####[52]####
        taskinfo.setParameters(childQueue, psManager);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, PSManager psManager) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, PSManager psManager, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setQueueArgIndexes(0);//####[52]####
        taskinfo.setIsPipeline(true);//####[52]####
        taskinfo.setParameters(childQueue, psManager);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskID<PSManager> psManager) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setTaskIdArgIndexes(1);//####[52]####
        taskinfo.addDependsOn(psManager);//####[52]####
        taskinfo.setParameters(childQueue, psManager);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[52]####
        taskinfo.addDependsOn(childQueue);//####[52]####
        taskinfo.addDependsOn(psManager);//####[52]####
        taskinfo.setParameters(childQueue, psManager);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, TaskID<PSManager> psManager, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setQueueArgIndexes(0);//####[52]####
        taskinfo.setIsPipeline(true);//####[52]####
        taskinfo.setTaskIdArgIndexes(1);//####[52]####
        taskinfo.addDependsOn(psManager);//####[52]####
        taskinfo.setParameters(childQueue, psManager);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, BlockingQueue<PSManager> psManager) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(PSPriorityQueueChild childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setQueueArgIndexes(1);//####[52]####
        taskinfo.setIsPipeline(true);//####[52]####
        taskinfo.setParameters(childQueue, psManager);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(TaskID<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setQueueArgIndexes(1);//####[52]####
        taskinfo.setIsPipeline(true);//####[52]####
        taskinfo.setTaskIdArgIndexes(0);//####[52]####
        taskinfo.addDependsOn(childQueue);//####[52]####
        taskinfo.setParameters(childQueue, psManager);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager) {//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return threadQueue(childQueue, psManager, new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> threadQueue(BlockingQueue<PSPriorityQueueChild> childQueue, BlockingQueue<PSManager> psManager, TaskInfo taskinfo) {//####[52]####
        // ensure Method variable is set//####[52]####
        if (__pt__threadQueue_PSPriorityQueueChild_PSManager_method == null) {//####[52]####
            __pt__threadQueue_PSPriorityQueueChild_PSManager_ensureMethodVarSet();//####[52]####
        }//####[52]####
        taskinfo.setQueueArgIndexes(0, 1);//####[52]####
        taskinfo.setIsPipeline(true);//####[52]####
        taskinfo.setParameters(childQueue, psManager);//####[52]####
        taskinfo.setMethod(__pt__threadQueue_PSPriorityQueueChild_PSManager_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    public void __pt__threadQueue(PSPriorityQueueChild childQueue, PSManager psManager) {//####[52]####
        System.out.println("THREAD DOING SHIT");//####[53]####
        PartialSolution ps = null;//####[54]####
        while (childQueue.hasNext()) //####[55]####
        {//####[55]####
            ps = childQueue.getCurrentPartialSolution();//####[56]####
            psManager.generateChildren(ps, childQueue);//####[57]####
        }//####[58]####
        ps = childQueue.getCurrentPartialSolution();//####[59]####
        System.out.println("8====D adding\n" + ps);//####[60]####
        _solutions.add(ps);//####[61]####
    }//####[62]####
//####[62]####
//####[64]####
    public PartialSolution findOptimal() throws ExecutionException, InterruptedException {//####[64]####
        PartialSolution[] ps = new PartialSolution[_cores];//####[65]####
        TaskIDGroup g = new TaskIDGroup(_cores);//####[66]####
        for (int i = 0; i < _cores; i++) //####[67]####
        {//####[67]####
            System.out.println("~~~~~ THread: " + i);//####[68]####
            PSManager psManager = new PSManager(_processors, _graph, _cache);//####[69]####
            TaskID id = threadQueue(_childQueues[i], psManager);//####[70]####
            System.out.println("THREAD ID: " + id);//####[71]####
            g.add(id);//####[72]####
            System.out.println("~~~~~ THread finished: " + i);//####[73]####
        }//####[74]####
        g.waitTillFinished();//####[75]####
        System.out.println("FINAL SOLUTION ~~~~~~~~~~~");//####[76]####
        for (PartialSolution p : _solutions) //####[77]####
        {//####[77]####
            System.out.println("NEWWWWWW\n" + p.toString());//####[78]####
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
//####[92]####
    public void printSolutions() {//####[92]####
        System.out.println("**************ZIHAO'S OUPUT********************");//####[94]####
        for (PartialSolution solution : _solutions) //####[96]####
        {//####[96]####
            System.out.println(solution.toString());//####[97]####
        }//####[98]####
        System.out.println("**************ZIHAO'S OUPUT********************");//####[100]####
    }//####[101]####
//####[103]####
    public void printChildQueues() {//####[103]####
        for (int i = 0; i < _childQueues.length; i++) //####[104]####
        {//####[104]####
            System.out.println("queue " + i);//####[106]####
            PSPriorityQueueChild queue = _childQueues[i];//####[108]####
            queue.printQueue();//####[109]####
        }//####[110]####
    }//####[111]####
}//####[111]####
