package com.gianghv.uniqlo.data

interface AppRepository {
    suspend fun isFirstRun(): Boolean
    suspend fun setFirstRun(isFirstRun: Boolean)
    suspend fun isLoggedIn(): Boolean
    suspend fun setLoggedIn(isLoggedIn: Boolean)
    suspend fun getUserId(): Long?
    suspend fun setUserId(userId: Long)
    suspend fun getRecommendationUrl(): String?
    suspend fun setRecommendationUrl(url: String)
    suspend fun getChatUrl(): String?
    suspend fun setChatUrl(url: String)
}
