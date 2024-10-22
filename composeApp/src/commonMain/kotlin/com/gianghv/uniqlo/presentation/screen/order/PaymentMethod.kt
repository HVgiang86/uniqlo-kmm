package com.gianghv.uniqlo.presentation.screen.order

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.ui.graphics.vector.ImageVector

sealed class PaymentMethod {
    data object Cash : PaymentMethod(), PaymentMethodBase {
        override fun getTitle(): String {
            return "Cash"
        }

        override fun getIcon(): ImageVector {
            return Icons.Default.Face
        }
    }

    data object VNPay : PaymentMethod(), PaymentMethodBase {
        override fun getTitle(): String {
            return "VNPay"
        }

        override fun getIcon(): ImageVector {
            return Icons.Default.Face
        }
    }
}

interface PaymentMethodBase {
    fun getTitle(): String
    fun getIcon(): ImageVector
}
