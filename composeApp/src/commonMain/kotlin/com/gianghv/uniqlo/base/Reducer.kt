package com.gianghv.uniqlo.base

import Uniqlo.composeApp.BuildConfig
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.yield

abstract class Reducer<S : Reducer.UiState, E : Reducer.UiEvent>(initialVal: S) {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialVal)
    val state: StateFlow<S>
        get() = _state.asStateFlow()

    val timeCapsule: TimeCapsule<S> = TimeTravelCapsule { storedState ->
        _state.tryEmit(storedState)
    }

    init {
        timeCapsule.addState(initialVal)
    }

    fun sendEvent(event: E) {
        reduce(_state.value, event)
    }

    fun setState(newState: S) {
        val success = _state.tryEmit(newState)

        if (BuildConfig.DEBUG && success) {
            // Only log in debug mode
            timeCapsule.addState(newState)
        }
    }

    abstract fun reduce(oldState: S, event: E)

    abstract class UiState(open val isLoading: Boolean = false, open val error: ErrorState? = null)
    interface UiEvent
}
