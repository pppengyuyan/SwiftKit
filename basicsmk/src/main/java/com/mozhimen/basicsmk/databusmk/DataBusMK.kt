package com.mozhimen.basicsmk.databusmk

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @ClassName DataBusMK
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/2/6 16:53
 * @Version 1.0
 */
object DataBusMK {
    private val eventMap = ConcurrentHashMap<String, StickyLiveData<*>>()
    fun <T> with(eventName: String): StickyLiveData<T> {
        //基于事件名称 订阅,分发消息
        //由于一个liveData只能发送 一种数据类型
        //所以 不同的event事件, 需要使用不同的liveData实例去分发
        var liveData = eventMap[eventName]
        if (liveData == null) {
            liveData = StickyLiveData<T>(eventName)
            eventMap[eventName] = liveData
        }
        return liveData as StickyLiveData<T>
    }

    class StickyLiveData<T>(private val eventName: String) : LiveData<T>() {
        var mStickyData: T? = null
        var mVersion = 0

        fun setStickyData(stickyData: T) {
            mStickyData = stickyData
            setValue(stickyData)
            //主线程去发送数据
        }

        fun postStickyData(stickyData: T) {
            mStickyData = stickyData
            postValue(stickyData)
            //不受线程的限制
        }

        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        override fun postValue(value: T) {
            mVersion++
            super.postValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observeSticky(owner, false, observer)
        }

        fun observeSticky(owner: LifecycleOwner, sticky: Boolean, observer: Observer<in T>) {
            //允许指定注册的观察者 是否需要关心黏性事件
            //sticky=true, 如果之前存在已经发送出去的数据, 那么这个observer会收到之前的黏性时间消息
            owner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                //监听宿主 发生销毁事件, 主动把liveData移除掉
                if (event == Lifecycle.Event.ON_DESTROY) {
                    eventMap.remove(eventName)
                }
            })
            super.observe(owner, StickyObserver(this, sticky, observer))
        }
    }

    //还有一种方法: 通过反射, 获取liveData当中的mVersion字段, 来控制黏性数据的分发与否, 但是我们认为这种反射不够优雅
    class StickyObserver<T>(private val stickyLiveData: StickyLiveData<T>, private val sticky: Boolean, private val observer: Observer<in T>) : Observer<T> {
        //lastVersion 和liveData的version对齐的原因, 就是为控制黏性事件的分发
        //sticky不等于true, 只能接收到注册之后发送的消息, 如果要接收黏性事件, 则sticky需要传递true
        private var lastVersion = stickyLiveData.mVersion
        override fun onChanged(t: T) {
            if (lastVersion >= stickyLiveData.mVersion) {
                //就说明stickyLiveData 没有更新的数据需要发送
                if (sticky && stickyLiveData.mStickyData != null) {
                    observer.onChanged(stickyLiveData.mStickyData)
                }
                return
            }

            lastVersion = stickyLiveData.mVersion
            observer.onChanged(t)
        }
    }
}