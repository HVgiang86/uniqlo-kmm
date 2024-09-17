package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.coredata.BaseRepository
import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.data.source.preferences.UserPreferences
import com.gianghv.uniqlo.data.source.preferences.UserPreferences.Keys.KEY_IS_LOGIN
import com.gianghv.uniqlo.data.source.preferences.UserPreferences.Keys.KEY_IS_ONBOARD_SHOWN

class AppRepositoryImpl(private val userPreferences: UserPreferences) : AppRepository, BaseRepository() {
    override suspend fun isFirstRun(): Boolean = userPreferences.getBoolean(KEY_IS_ONBOARD_SHOWN, defaultValue = true)
    override suspend fun setFirstRun(isFirstRun: Boolean) = userPreferences.putBoolean(KEY_IS_ONBOARD_SHOWN, isFirstRun)
    override suspend fun isLoggedIn(): Boolean = userPreferences.getBoolean(KEY_IS_LOGIN)
    override suspend fun setLoggedIn(isLoggedIn: Boolean) = userPreferences.putBoolean(KEY_IS_LOGIN, isLoggedIn)
    override suspend fun getUserId(): Long? = userPreferences.getLong(UserPreferences.KEY_USER_ID)
    override suspend fun setUserId(userId: Long) = userPreferences.putLong(UserPreferences.KEY_USER_ID, userId)
}
