package com.no_more.base.utils

import androidx.fragment.app.Fragment
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

inline fun <T> Flow<T>.collectLatestState(
    fragment: Fragment,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: suspend (data: T) -> Unit
): Job = this.launchCollectLatestRepeatWithState(
    lifecycleOwner = fragment.viewLifecycleOwner,
    state = state,
    runBlock = runBlock
)

inline fun <T> Flow<T>.collectEvent(
    fragment: Fragment,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline runBlock: (data: T) -> Unit
): Job = this.launchCollectRepeatWithState(
    lifecycleOwner = fragment.viewLifecycleOwner,
    state = state,
    runBlock = runBlock
)
