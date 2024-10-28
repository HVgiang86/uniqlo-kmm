package com.gianghv.uniqlo.presentation.screen.orderhistory

enum class OrderStatus {
    PENDING, ACCEPTED, USER_DENY, ADMIN_DENY, OTHER
}

fun String.toOrderStatus(): OrderStatus {
    return when (this) {
        "pending" -> OrderStatus.PENDING
        "accept" -> OrderStatus.ACCEPTED
        "user_deny" -> OrderStatus.USER_DENY
        "admin_deny" -> OrderStatus.ADMIN_DENY
        else -> OrderStatus.OTHER
    }
}

fun OrderStatus.toText(): String {
    return when (this) {
        OrderStatus.PENDING -> "Chờ xác nhận"
        OrderStatus.ACCEPTED -> "Đã xác nhận"
        OrderStatus.USER_DENY -> "Đã hủy"
        OrderStatus.ADMIN_DENY -> "Bị từ chối"
        OrderStatus.OTHER -> "Khác"
    }
}
