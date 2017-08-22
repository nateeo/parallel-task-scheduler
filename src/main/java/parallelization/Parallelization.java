package parallelization;//####[1]####
//####[1]####
import algorithm.PSManager;//####[3]####
import algorithm.PSPriorityQueue;//####[4]####
import algorithm.PartialSolution;//####[5]####
import graph.Graph;//####[6]####
import java.util.ArrayList;//####[8]####
import java.util.HashMap;//####[9]####
import java.util.Map;//####[10]####
import java.util.PriorityQueue;//####[11]####
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
    private PSManager _psManager;//####[22]####
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
//####[30]####
    public Parallelization (PSPriorityQueue priorityQueue, int processors, Graph graph, int cores) {//####[30]####
        _parentQueue = priorityQueue;//####[31]####
        _processors = processors;//####[32]####
        _graph = graph;//####[33]####
        _cores = cores;//####[34]####
        _childQueues = new PSPriorityQueueChild[cores];//####[35]####
        _solutions = new ArrayList<PartialSolution>();//####[36]####
        initialiseQueues();//####[38]####
    }//####[40]####
//####[42]####
    public void initialiseQueues() {//####[42]####
        PSPriorityQueueChild[] queues = _parentQueue.splitQueue(_cores);//####[43]####
        for (int i = 0; i < _cores; i++) //####[45]####
        {//####[45]####
            _childQueues[i] = queues[i];//####[46]####
        }//####[47]####
        System.out.println("************Child queues************");//####[48]####
        printChildQueues();//####[49]####
        System.out.println("************Child Queues************");//####[50]####
    }//####[51]####
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
        System.out.println("THREAD DOING SHIT");//####[54]####
        PartialSolution ps = null;//####[55]####
        while (childQueue.hasNext()) //####[56]####
        {//####[56]####
            ps = childQueue.getCurrentPartialSolution();//####[57]####
            psManager.generateChildren(ps, childQueue);//####[58]####
        }//####[59]####
        ps = childQueue.getCurrentPartialSolution();//####[60]####
        System.out.println("8====D adding\n" + ps);//####[61]####
        _solutions.add(ps);//####[62]####
    }//####[63]####
//####[63]####
//####[65]####
    public PartialSolution findOptimal() throws ExecutionException, InterruptedException {//####[65]####
        PartialSolution[] ps = new PartialSolution[_cores];//####[66]####
        TaskIDGroup g = new TaskIDGroup(_cores);//####[67]####
        for (int i = 0; i < _cores; i++) //####[68]####
        {//####[68]####
            System.out.println("~~~~~ THread: " + i);//####[69]####
            PSManager psManager = new PSManager(_processors, _graph);//####[70]####
            TaskID id = threadQueue(_childQueues[i], psManager);//####[71]####
            System.out.println("THREAD ID: " + id);//####[72]####
            g.add(id);//####[73]####
            System.out.println("~~~~~ THread finished: " + i);//####[74]####
        }//####[75]####
        g.waitTillFinished();//####[76]####
        System.out.println("FINAL SOLUTION ~~~~~~~~~~~");//####[77]####
        for (PartialSolution p : _solutions) //####[78]####
        {//####[78]####
            System.out.println("NEWWWWWW\n" + p.toString());//####[79]####
        }//####[80]####
        PartialSolution solution = null;//####[81]####
        int finalTime = -1;//####[82]####
        for (int i = 0; i < _solutions.size(); i++) //####[83]####
        {//####[83]####
            int psFinishTime = _solutions.get(i)._latestSlot.getFinish();//####[84]####
            if (finalTime == -1 || psFinishTime < finalTime) //####[85]####
            {//####[85]####
                solution = _solutions.get(i);//####[86]####
                finalTime = psFinishTime;//####[87]####
            }//####[88]####
        }//####[89]####
        return solution;//####[90]####
    }//####[91]####
//####[93]####
    public void printSolutions() {//####[93]####
        System.out.println("**************ZIHAO'S OUPUT********************");//####[95]####
        for (PartialSolution solution : _solutions) //####[97]####
        {//####[97]####
            System.out.println(solution.toString());//####[98]####
        }//####[99]####
        System.out.println("**************ZIHAO'S OUPUT********************");//####[101]####
    }//####[102]####
//####[104]####
    public void printChildQueues() {//####[104]####
        for (int i = 0; i < _childQueues.length; i++) //####[105]####
        {//####[105]####
            System.out.println("queue " + i);//####[107]####
            PSPriorityQueueChild queue = _childQueues[i];//####[109]####
            queue.printQueue();//####[110]####
        }//####[111]####
    }//####[112]####
}//####[112]####
