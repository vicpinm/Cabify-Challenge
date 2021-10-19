package com.vicpin.cabifychallenge.domain.usecase

import kotlinx.coroutines.*

abstract class UseCase<T> {

    private var myJob: Job? = null

    operator fun invoke(onResult: (T) -> Unit) {
        myJob = CoroutineScope(Dispatchers.IO).launch {
            val result = doOnBackground()
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }

    abstract suspend fun doOnBackground(): T

    fun cancel() = myJob?.cancel()
}