package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.domain.SaveOrderInfo
import com.gianghv.uniqlo.presentation.screen.order.PaymentMethod
import com.gianghv.uniqlo.presentation.screen.order.PaymentMethodBase

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
    suspend fun setSavedOrderInfo(
        userId: Long = -1L, address: String? = "", email: String? = "", phone: String? = "", paymentMethod: PaymentMethodBase? = PaymentMethod.Cash
    )

    suspend fun getSavedOrderInfo(): SaveOrderInfo?
}
