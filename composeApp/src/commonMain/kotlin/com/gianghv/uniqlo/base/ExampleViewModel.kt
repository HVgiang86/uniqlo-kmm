package com.gianghv.uniqlo.base

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.StateFlow

class ExampleViewModel : BaseViewModel<MainState, MainUiEvent>() {
    override val reducer = MainReducer(MainState.initial())
    override fun onClearLoadingState() {

    }

    override fun onClearErrorState() {
    }

    override val onException: ((Throwable) -> Unit)
        get() = {}

    override val state: StateFlow<MainState>
        get() = reducer.state

    val timeMachine: TimeCapsule<MainState> get() = reducer.timeCapsule
}

@Immutable
data class MainState(
    override val isLoading: Boolean, override val error: ErrorState?, val data: String
) : Reducer.UiState(isLoading, error) {
    companion object {
        fun initial() = MainState(
            isLoading = true, error = null, data = "Haha Init"
        )
    }
}

@Immutable
sealed class MainUiEvent : Reducer.UiEvent {
    data class ShowData(val text: String) : MainUiEvent()
}

class MainReducer(initialVal: MainState) : Reducer<MainState, MainUiEvent>(initialVal) {
    override fun reduce(oldState: MainState, event: MainUiEvent) {
        when (event) {
            is MainUiEvent.ShowData -> {
                setState(oldState.copy(isLoading = false, data = "$oldState+"))
            }
        }
    }
}
