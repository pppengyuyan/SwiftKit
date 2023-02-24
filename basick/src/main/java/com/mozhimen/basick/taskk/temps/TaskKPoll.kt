package com.mozhimen.basick.taskk.temps

import com.mozhimen.basick.taskk.bases.BaseTaskK
import kotlinx.coroutines.*

class TaskKPoll : BaseTaskK() {
    private var _pollingScope: CoroutineScope? = null

    fun isActive(): Boolean =
        _pollingScope != null && _pollingScope!!.isActive

    fun start(interval: Long, task: suspend () -> Unit) {
        if (isActive()) return
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            while (isActive) {
                try {
                    task.invoke()
                } catch (e: Exception) {
                    if (e is CancellationException) return@launch
                    e.printStackTrace()
                }
                delay(interval)
            }
        }
        _pollingScope = scope
    }

    override fun cancel() {
        if (!isActive()) return
        _pollingScope?.cancel()
        _pollingScope = null
    }
}