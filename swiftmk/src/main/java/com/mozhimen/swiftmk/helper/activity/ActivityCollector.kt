package com.mozhimen.swiftmk.helper.activity

import android.app.Activity

/**
 * @ClassName ActivityCollector
 * @Description TODO
 * @Author mozhimen
 * @Date 2021/4/14 17:04
 * @Version 1.0
 */
object ActivityCollector {
    private val activities=ArrayList<Activity>()

    fun addActivity(activity: Activity){
        activities.add(activity)
    }

    fun removeActivity(activity: Activity){
        activities.remove(activity)
    }

    /**
     * 作用: 销毁所有的Activity
     * 用法: ActivityCollector.finishAll()
     * 进程销毁:android.os.Process.killProcess(android.os.Process.myPid())
     */
    fun finishAll(){
        for (activity in activities){
            if(!activity.isFinishing){
                activity.finish()
            }
        }
        activities.clear()
    }
}