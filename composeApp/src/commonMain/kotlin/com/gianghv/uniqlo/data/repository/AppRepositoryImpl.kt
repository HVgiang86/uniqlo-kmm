package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.coredata.BaseRepository
import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.data.source.preferences.UserPreferences
import com.gianghv.uniqlo.data.source.preferences.UserPreferences.Keys.KEY_IS_LOGIN
import com.gianghv.uniqlo.data.source.preferences.UserPreferences.Keys.KEY_IS_ONBOARD_SHOWN
import com.gianghv.uniqlo.domain.SaveOrderInfo
import com.gianghv.uniqlo.presentation.screen.order.PaymentMethodBase
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppRepositoryImpl(private val userPreferences: UserPreferences) : AppRepository, BaseRepository() {
    override suspend fun isFirstRun(): Boolean = userPreferences.getBoolean(KEY_IS_ONBOARD_SHOWN, defaultValue = true)
    override suspend fun setFirstRun(isFirstRun: Boolean) = userPreferences.putBoolean(KEY_IS_ONBOARD_SHOWN, isFirstRun)
    override suspend fun isLoggedIn(): Boolean = userPreferences.getBoolean(KEY_IS_LOGIN)
    override suspend fun setLoggedIn(isLoggedIn: Boolean) = userPreferences.putBoolean(KEY_IS_LOGIN, isLoggedIn)
    override suspend fun getUserId(): Long? {
        AppLogger.d("hehe")
        val string = userPreferences.getString(UserPreferences.KEY_USER_ID)
        if (string.isNullOrEmpty()) return null
        return string.toLong()
    }

    override suspend fun setUserId(userId: Long) = userPreferences.putString(UserPreferences.KEY_USER_ID, userId.toString())
    override suspend fun getRecommendationUrl(): String? = userPreferences.getString(UserPreferences.KEY_RECOMMENDATION_URL)

    override suspend fun setRecommendationUrl(url: String) = userPreferences.putString(UserPreferences.KEY_RECOMMENDATION_URL, url)

    override suspend fun getChatUrl(): String? = userPreferences.getString(UserPreferences.KEY_CHAT_URL)

    override suspend fun setChatUrl(url: String) = userPreferences.putString(UserPreferences.KEY_CHAT_URL, url)
    override suspend fun setSavedOrderInfo(userId: Long, address: String?, email: String?, phone: String?, paymentMethod: PaymentMethodBase?) {
        val saveOrderInfoList = getAllSavedOrderInfo().toMutableList()

        val find = saveOrderInfoList.find { it.id == userId }

        val saveOrderInfo = SaveOrderInfo(userId, address, email, phone, paymentMethod?.getName() ?: "")

        if (find != null) {
            saveOrderInfoList.remove(find)
            saveOrderInfoList.add(saveOrderInfo)
        } else {
            saveOrderInfoList.add(saveOrderInfo)
        }

        userPreferences.putString(UserPreferences.KEY_ORDER_INFO, Json.encodeToString(saveOrderInfoList))
    }

    override suspend fun getSavedOrderInfo(): SaveOrderInfo? {
        val json = userPreferences.getString(UserPreferences.KEY_ORDER_INFO)
        if (json.isNullOrEmpty()) return null
        val saveOrderInfoList = Json.decodeFromString<List<SaveOrderInfo>>(json)
        return saveOrderInfoList.find { it.id == WholeApp.USER_ID }
    }

    private suspend fun getAllSavedOrderInfo(): List<SaveOrderInfo> {
        val json = userPreferences.getString(UserPreferences.KEY_ORDER_INFO)
        if (json.isNullOrEmpty()) return emptyList()
        return Json.decodeFromString<List<SaveOrderInfo>>(json)
    }

}
