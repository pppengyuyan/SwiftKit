package com.mozhimen.basick.flowk.mos

import androidx.core.os.TraceCompat
import com.mozhimen.basick.flowk.annors.AFlowKState
import com.mozhimen.basick.flowk.commons.IFlowKListener
import com.mozhimen.basick.flowk.helpers.FlowKRuntime
import com.mozhimen.basick.flowk.helpers.RuntimeCallback
import com.mozhimen.basick.flowk.helpers.NodeComparator
import java.util.*
import kotlin.collections.ArrayList

/**
 * @ClassName TaskK
 * @Description 启动阶段需要初始化的任务，在taskk中对应着一个Task
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/3/29 15:15
 * @Version 1.0
 */
abstract class FlowKNode @JvmOverloads constructor(
    /**任务名称**/
    val id: String,
    /**是否是异步任务**/
    val isAsyncTask: Boolean = false,
    /**延迟执行的时间**/
    val delayMills: Long = 0,
    /**任务的优先级**/
    var priority: Int = 0
) : Runnable, Comparable<FlowKNode> {
    var executeTime: Long = 0
        //任务执行时间
        protected set
    var state: Int = AFlowKState.IDLE
        //任务的状态
        protected set

    val dependTasks: MutableList<FlowKNode> = ArrayList()//当前task依赖了那些前置任务，只有当dependTasks集合中的所有任务执行完，当前才可以被执行
    val behindTasks: MutableList<FlowKNode> = ArrayList()//当前task被那些后置任务依赖，只有当当前这个task执行完，behindTasks集合中的后置任务才可以执行
    private val _iTaskKListeners: MutableList<IFlowKListener> = ArrayList()//任务运行状态监听器集
    private var _taskKRuntimeListener: RuntimeCallback? = RuntimeCallback()//用于输出task运行时的日志
    val dependTasksName: MutableList<String> = ArrayList()//用于运行时log统计输出，输出当前task依赖了那些前置任务， 这些前置任务的名称我们将它存储在这里

    open fun start() {
        if (state != AFlowKState.IDLE) {
            throw RuntimeException("cannot run task $id again")
        }
        toStart()
        executeTime = System.currentTimeMillis()
        //执行当前任务
        FlowKRuntime.executeTask(this)
    }

    fun addTaskKListener(ITaskKListener: IFlowKListener) {
        if (!_iTaskKListeners.contains(ITaskKListener)) {
            _iTaskKListeners.add(ITaskKListener)
        }
    }

    //给当前task添加-个前置的依赖任务
    open fun dependOn(dependFlowKNode: FlowKNode) {
        var taskK = dependFlowKNode
        if (taskK != this) {
            if (dependFlowKNode is FlowKNodeGroup) {
                taskK = dependFlowKNode.endTask
                dependTasks.add(taskK)
                dependTasksName.add(taskK.id)
                //当前task依赖了dependTask， 那么我们还需要吧dependTask-里面的behindTask添加进去当前的task
                if (!taskK.behindTasks.contains(this)) {
                    taskK.behindTasks.add(this)
                }
            }
        }
    }

    //给当前task移除一个前置依赖任务
    open fun removeDependence(dependTask: FlowKNode) {
        var taskK = dependTask
        if (dependTask != this) {
            if (dependTask is FlowKNodeGroup) {
                taskK = dependTask.endTask
            }
            dependTasks.remove(taskK)
            dependTasksName.remove(taskK.id)
            //把当前task从dependTask的 后置依赖任务集合behindTasks中移除
            //达到接触两个任务依赖关系的目的
            if (taskK.behindTasks.contains(this)) {
                taskK.behindTasks.remove(this)
            }
        }
    }

    //给当前任务添加后置依赖项
    //他和dependOn 是相反的
    open fun behind(behindTask: FlowKNode) {
        var taskK = behindTask
        if (behindTask != this) {
            if (behindTask is FlowKNodeGroup) {
                taskK = behindTask.startTask
            }
            //这个是把behindTask添加到当前task的后面
            behindTasks.add(taskK)
            //把当前task添加到behindTask 的前面
            behindTask.dependOn(this)
        }
    }

    //给当前task移除-个后置的任务
    open fun removeBehind(behindTask: FlowKNode) {
        var taskK = behindTask
        if (behindTask != this) {
            if (behindTask is FlowKNodeGroup) {
                taskK = behindTask.startTask
            }
            behindTasks.remove(taskK)
            behindTask.removeDependence(this)
        }
    }

    override fun run() {
        //改变任务的状态--onStart onRunning onFinished -- 通知后置任务去开始执行
        TraceCompat.beginSection(id)
        toRunning()
        run(id)//真正的执行初始化任务的代码的方法
        toFinish()
        //通知它的后置任务去执行
        notifyBehindTasks()
        recycle()
        TraceCompat.endSection()
    }

    private fun recycle() {
        dependTasks.clear()
        behindTasks.clear()
        _iTaskKListeners.clear()
        _taskKRuntimeListener = null
    }

    private fun notifyBehindTasks() {
        //通知后置任务去尝试执行
        if (behindTasks.isNotEmpty()) {
            if (behindTasks.size > 1) {
                Collections.sort(behindTasks, FlowKRuntime.flowKNodeComparator)
            }

            //遍历behindTask后置任务，通知他们，告诉他们你的一个前置依赖任务已经执行完成了
            for (behindTask in behindTasks) {
                // A behindTask ->(B,C) A执行完成之后， B,C才可以执行。
                behindTask.dependTaskFinished(this)
            }

        }
    }

    private fun dependTaskFinished(dependFlowKNode: FlowKNode) {
        // A behindTasks ->(B,C) A执行完成之后， B,C7可以执行。
        // task= B,C , dependTask=A
        if (dependTasks.isEmpty()) {
            return
        }
        //把A从B, C的前置依赖任务集合中移除
        dependTasks.remove(dependFlowKNode)
        //B, C的所有前置任务是否都执行完了
        if (dependTasks.isEmpty()) {
            start()
        }
    }

    private fun toStart() {
        state = AFlowKState.START
        FlowKRuntime.setStateInfo(this)
        for (listener in _iTaskKListeners) {
            listener.onStart(this)
        }
        _taskKRuntimeListener?.onStart(this)
    }

    private fun toFinish() {
        state = AFlowKState.FINISHED
        FlowKRuntime.setStateInfo(this)
        FlowKRuntime.removeBlockTask(this.id)
        for (listener in _iTaskKListeners) {
            listener.onFinished(this)
        }
        _taskKRuntimeListener?.onFinished(this)
    }

    private fun toRunning() {
        state = AFlowKState.RUNNING
        FlowKRuntime.setStateInfo(this)
        FlowKRuntime.setThreadName(this,Thread.currentThread().name)
        for (listener in _iTaskKListeners) {
            listener.onRunning(this)
        }
        _taskKRuntimeListener?.onRunning(this)
    }

    abstract fun run(id: String)

    override fun compareTo(other: FlowKNode): Int {
        return NodeComparator.compareNode(this, other)
    }
}

