package com.gianghv.uniqlo.base

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.ScreenModelStore
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.plus
import org.koin.compose.getKoin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier


abstract class BaseViewModel<T : Reducer.UiState, E : Reducer.UiEvent> : ScreenModel {

    abstract val state: StateFlow<T>
    abstract val reducer: Reducer<T, E>

    abstract fun onClearLoadingState()
    abstract fun onClearErrorState()

    fun sendEvent(event: E) {
        reducer.sendEvent(event)
    }

    abstract val onException: ((Throwable) -> Unit)

    protected var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onException.let { it(throwable) }
    }
}

fun BaseViewModel<*, *>.uiStateHolderScope(dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate): CoroutineScope {
    return ScreenModelStore.getOrPutDependency(screenModel = this,
        name = "ScreenModelCoroutineScope",
        factory = { key -> CoroutineScope(SupervisorJob() + dispatcher) + CoroutineName(key) },
        onDispose = { scope -> scope.cancel() })
}

@Composable
inline fun <reified T : BaseViewModel<*, *>> Screen.getViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val koin = getKoin()
    return rememberScreenModel(tag = qualifier?.value) { koin.get(qualifier, parameters) }
}


