package com.gianghv.uniqlo.base

import kotlinx.datetime.Clock

data class ErrorState(
    val throwable: Throwable? = null,
    val shouldShowDialog: Boolean = false,
    val timeStamp: Long = Clock.System.now().epochSeconds
)
