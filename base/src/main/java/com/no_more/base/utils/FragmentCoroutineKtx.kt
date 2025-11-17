package com.no_more.base.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.withCreated
import androidx.lifecycle.withResumed
import androidx.lifecycle.withStarted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/*Fragment launch When (repeat)*/
inline fun Fragment.launchWhenCreated(crossinline block: () -> Unit): Job? {
    if (this.isAdded.not()) return null
    return viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            block()
        }
    }
}

inline fun Fragment.launchWhenResumed(crossinline block: () -> Unit): Job? {
    if (this.isAdded.not()) return null
    return viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            block()
        }
    }
}

inline fun Fragment.launchWhenStarted(crossinline block: () -> Unit): Job? {
    if (this.isAdded.not()) return null
    return viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

/*Fragment launch In*/
inline fun Fragment.launchInCreated(crossinline block: CoroutineScope.() -> Unit): Job? {
    if (this.isAdded.not()) return null
    return viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.withCreated { block() }
    }
}

inline fun Fragment.launchInResumed(crossinline block: CoroutineScope.() -> Unit): Job? {
    if (this.isAdded.not()) return null
    return viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.withResumed { block() }
    }
}

inline fun Fragment.launchInStarted(crossinline block: CoroutineScope.() -> Unit): Job? {
    if (this.isAdded.not()) return null
    return viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.withStarted { block() }
    }
}

