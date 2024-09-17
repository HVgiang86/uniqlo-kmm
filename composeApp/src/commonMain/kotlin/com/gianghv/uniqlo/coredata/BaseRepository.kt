package com.gianghv.uniqlo.coredata

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import uniqlo.composeapp.generated.resources.Res
import kotlin.coroutines.CoroutineContext

abstract class BaseRepository: KoinComponent {
    private val ioDispatcher: CoroutineDispatcher by inject()

    protected fun getContext() = ioDispatcher

    /**
     * Make template code to get data with flow
     * @return a flow of data
     */
    protected suspend fun <R> flowContext(
        context: CoroutineContext = getContext(),
        requestBlock: suspend CoroutineScope.() -> Result<R>,
    ): Flow<R> =
        withContext(context) {
            flow {
                when (val result = requestBlock()) {
                    is Result.Error -> {
                        throw result.throwable
                    }

                    is Result.Success -> {
                        emit(result.data)
                    }
                }
            }
        }

    protected suspend fun <R> flowContextWithoutException(
        context: CoroutineContext = getContext(),
        requestBlock: suspend CoroutineScope.() -> Result<R>,
    ): Flow<R> =
        withContext(context) {
            flow {
                when (val result = requestBlock()) {
                    is Result.Error -> {
                        cancel()
                    }

                    is Result.Success -> {
                        emit(result.data)
                    }
                }
            }
        }
}
