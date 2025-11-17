package com.no_more.base.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

inline fun <T> Flow<T>.collectLatestState(
    fragment: Fragment,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: suspend (data: T) -> Unit
): Job = this.launchCollectLatestRepeatWithState(
    lifecycleOwner = fragment.viewLifecycleOwner, state = state, runBlock = runBlock
)

inline fun <T> Flow<T>.collectEvent(
    fragment: Fragment,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: (data: T) -> Unit
): Job = this.launchCollectRepeatWithState(
    lifecycleOwner = fragment.viewLifecycleOwner, state = state, runBlock = runBlock
)
