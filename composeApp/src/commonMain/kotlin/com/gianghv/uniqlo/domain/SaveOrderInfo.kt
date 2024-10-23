package com.gianghv.uniqlo.domain

import com.gianghv.uniqlo.presentation.screen.order.PaymentMethod
import kotlinx.serialization.Serializable

@Serializable
data class SaveOrderInfo(
    val id: Long? = 0L, val address: String? = "", val email: String? = "", val phone: String? = "", val paymentMethod: String? = PaymentMethod.Cash.getName()
)
