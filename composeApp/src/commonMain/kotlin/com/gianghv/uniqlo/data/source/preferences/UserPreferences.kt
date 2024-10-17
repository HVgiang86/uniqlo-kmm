package com.gianghv.uniqlo.data.source.preferences

interface UserPreferences {

    companion object Keys {
        const val KEY_IS_ONBOARD_SHOWN = "KEY_IS_ONBOARD_SHOWN"
        const val KEY_IS_LOGIN = "KEY_IS_LOGIN"
        const val KEY_USER_ID = "KEY_USER_ID"
        const val TOKEN = "TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val KEY_RECOMMENDATION_URL = "RECOMMENDATION_URL"
        const val KEY_CHAT_URL = "CHAT_URL"
    }

    suspend fun getString(key: String, defaultValue: String? = null): String?
    suspend fun getInt(key: String, defaultValue: Int? = null): Int?
    suspend fun getLong(key: String, defaultValue: Long? = null): Long?
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    suspend fun putString(key: String, value: String)
    suspend fun putInt(key: String, value: Int)
    suspend fun putLong(key: String, value: Long)
    suspend fun putBoolean(key: String, value: Boolean)

    suspend fun clear()
}
