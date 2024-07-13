package com.view.wordwise

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> StateFlow<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T = runBlocking {
    var data: T? = null
    val job = withTimeout(timeUnit.toMillis(time)) {
        data = this@getOrAwaitValue.value
    }
    afterObserve.invoke()
    if (data == null) {
        throw TimeoutException("StateFlow value was never set.")
    }
    return@runBlocking data as T
}
