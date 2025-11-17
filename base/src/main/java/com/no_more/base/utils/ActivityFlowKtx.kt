package com.no_more.base.utils

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

/*Activity Collect*/
inline fun <T> Flow<T>.collectLatestState(
    activity: ComponentActivity,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: suspend (data: T) -> Unit
): Job = this.launchCollectLatestRepeatWithState(
    lifecycleOwner = activity,
    state = state,
    runBlock = runBlock
)

inline fun <T> Flow<T>.collectEvent(
    activity: ComponentActivity,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: (data: T) -> Unit
): Job = this.launchCollectRepeatWithState(
    lifecycleOwner = activity,
    state = state,
    runBlock = runBlock
)
