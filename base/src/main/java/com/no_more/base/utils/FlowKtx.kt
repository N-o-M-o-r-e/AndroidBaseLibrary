@file:OptIn(FlowPreview::class)

package com.no_more.base.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.launchCollectLatestRepeatWithState(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: suspend (data: T) -> Unit
): Job = lifecycleOwner.lifecycleScope.launch {
    lifecycleOwner.repeatOnLifecycle(state) {
        this@launchCollectLatestRepeatWithState.collectLatest {
            runBlock(it)
        }
    }
}

inline fun <T> Flow<T>.launchCollectRepeatWithState(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: (data: T) -> Unit
): Job = lifecycleOwner.lifecycleScope.launch {
    lifecycleOwner.repeatOnLifecycle(state) {
        this@launchCollectRepeatWithState.collect {
            runBlock(it)
        }
    }
}

/*Suspend to flow*/
inline fun <reified T> flowOfSuspend(crossinline function: suspend () -> T) = flow {
    emit(function())
}

@OptIn(FlowPreview::class)
fun Flow<Boolean>.mapLoadingFlow(): Flow<Boolean> = this
    .distinctUntilChanged()
    .debounce { if (it) 0L else 100L }
    .distinctUntilChanged()

@OptIn(FlowPreview::class)
fun Flow<Boolean>.mapErrorFlow(): Flow<Boolean> = this
    .distinctUntilChanged()
    .debounce { if (it) 100L else 0L }
    .distinctUntilChanged()

