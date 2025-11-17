package com.no_more.base.abs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
abstract class AbsViewModel<A, E, S> : ViewModel() {
    // State
    abstract fun initUiState(): S

    private val _viewState by lazy { MutableStateFlow<S>(initUiState()) }
    val stateFlow: Flow<S> by lazy { _viewState.asStateFlow() }
    val stateValue: S get() = _viewState.value
    fun updateState(function: S.() -> S) = _viewState.update(function)

    protected val sharedAction = MutableSharedFlow<A>()

    private suspend fun awaitActionSubscribed() {
        sharedAction.subscriptionCount.debounce { if (it > 0) 0 else 10 }.filter { it > 0 }.first()
    }

    fun dispatch(action: A) {
        viewModelScope.launch {
            awaitActionSubscribed()
            sharedAction.emit(action)
        }
    }

    protected inline fun <reified T : A> actionFlow(): Flow<T> = sharedAction.filterIsInstance<T>()

    // Events
    private val channel by lazy { Channel<E>(Channel.UNLIMITED) }
    fun <T : E> sendEvent(event: T) = channel.trySend(event)
    val eventFlow: Flow<E> by lazy { channel.receiveAsFlow() }

    private val eventBags: MutableSet<Channel<*>> by lazy { mutableSetOf() }

    override fun onCleared() {
        super.onCleared()
        eventBags.forEach { it.cancel() }
    }

    protected fun <T> Channel<T>.addToBag() = this.apply {
        eventBags.add(this)
    }

    /*ViewModel*/
    inline fun <T> Flow<T>.collectIn(
        crossinline collectionBlock: CoroutineScope.(data: T) -> Unit
    ): Job = viewModelScope.launch {
        this@collectIn.collect {
            collectionBlock(it)
        }
    }

    inline fun <T> Flow<T>.collectLatestIn(
        crossinline collectionBlock: suspend CoroutineScope.(data: T) -> Unit
    ): Job = viewModelScope.launch {
        this@collectLatestIn.collectLatest {
            collectionBlock(it)
        }
    }

    inline fun <T> Flow<T>.collectMergeIn(
        crossinline collectionBlock: suspend CoroutineScope.(data: T) -> Unit
    ): Job = viewModelScope.launch {
        this@collectMergeIn
            .flatMapMerge {
                flow {
                    emit(collectionBlock(it))
                }
            }
            .collect()
    }

    inline fun <T> Flow<T>.collectFirstIn(
        crossinline collectionBlock: suspend (data: T) -> Unit
    ): Job = this@collectFirstIn.conflate().onEach {
        collectionBlock(it)
    }.launchIn(viewModelScope)
}