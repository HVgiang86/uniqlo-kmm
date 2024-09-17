package com.gianghv.uniqlo.data

interface AppRepository {
    suspend fun isFirstRun(): Boolean
    suspend fun setFirstRun(isFirstRun: Boolean)
    suspend fun isLoggedIn(): Boolean
    suspend fun setLoggedIn(isLoggedIn: Boolean)
    suspend fun getUserId(): String?
}
