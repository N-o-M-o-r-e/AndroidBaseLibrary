package com.no_more.base.utils

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withCreated
import androidx.lifecycle.withResumed
import androidx.lifecycle.withStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/*Activity launch when(repeat)*/
inline fun ComponentActivity.launchWhenCreated(
    crossinline block: () -> Unit
): Job = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.CREATED) { block() }
}

inline fun ComponentActivity.launchWhenResumed(
    crossinline block: () -> Unit
): Job = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.RESUMED) { block() }
}

inline fun ComponentActivity.launchWhenStarted(
    crossinline block: () -> Unit
): Job = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) { block() }
}

/*Activity launch In*/
inline fun ComponentActivity.launchInCreated(
    crossinline block: CoroutineScope.() -> Unit
): Job = lifecycleScope.launch {
    withCreated { block() }
}

inline fun ComponentActivity.launchInResumed(
    crossinline block: CoroutineScope.() -> Unit
): Job = lifecycleScope.launch {
    withResumed { block() }
}

inline fun ComponentActivity.launchInStarted(
    crossinline block: CoroutineScope.() -> Unit
): Job = lifecycleScope.launch {
    withStarted { block() }
}
