package com.gianghv.uniqlo.presentation.screen.main

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.MainReducer
import com.gianghv.uniqlo.base.MainState
import com.gianghv.uniqlo.base.MainUiEvent
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.TimeCapsule
import com.gianghv.uniqlo.base.uiStateHolderScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel : BaseViewModel<MainState, MainUiEvent>() {
    override val reducer = MainReducer(MainState.initial())
    override fun onClearLoadingState() {

    }

    override fun onClearErrorState() {
    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(MainUiEvent.ShowData("Error"))
        }

    override val state: StateFlow<MainState>
        get() = reducer.state
            .stateIn(uiStateHolderScope(Dispatchers.Main.immediate), SharingStarted.WhileSubscribed(5000),MainState.initial())
    val timeMachine: TimeCapsule<MainState> get() = reducer.timeCapsule
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
