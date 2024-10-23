package com.gianghv.uniqlo.presentation.screen.order

import androidx.compose.ui.graphics.vector.ImageVector
import com.gianghv.uniqlo.theme.icons.CashStack
import com.gianghv.uniqlo.theme.icons.IC_BANK

sealed class PaymentMethod {
    data object Cash : PaymentMethod(), PaymentMethodBase {
        override fun getTitle(): String {
            return "Cash"
        }

        override fun getIcon(): ImageVector {
            return CashStack
        }
    }

    data object VNPay : PaymentMethod(), PaymentMethodBase {
        override fun getTitle(): String {
            return "VNPay"
        }

        override fun getIcon(): ImageVector {
            return IC_BANK
        }
    }
}

interface PaymentMethodBase {
    fun getTitle(): String
    fun getIcon(): ImageVector

    fun getName(): String {
        return this::class.simpleName ?: ""
    }

    companion object {
        fun fromName(name: String): PaymentMethodBase {
            return when (name) {
                PaymentMethod.Cash::class.simpleName -> PaymentMethod.Cash
                PaymentMethod.VNPay::class.simpleName -> PaymentMethod.VNPay
                else -> PaymentMethod.Cash
            }
        }
    }
}
