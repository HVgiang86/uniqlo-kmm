package com.gianghv.uniqlo.presentation.screen.main

import androidx.compose.runtime.Immutable
import com.gianghv.uniqlo.base.Reducer

@Immutable
sealed class MainUiEvent : Reducer.UiEvent {
    data class ShowData(val text: String) : MainUiEvent()
}
