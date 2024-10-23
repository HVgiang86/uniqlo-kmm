package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.data.source.remote.request.CreateOrderRequest
import com.gianghv.uniqlo.data.source.remote.response.CreateCartResponse
import com.gianghv.uniqlo.data.source.remote.response.CreateOrderResponse
import com.gianghv.uniqlo.data.source.remote.response.UpdateQuantityResponse
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.domain.OrderHistory
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getCartItems(userId: Long): Flow<List<CartItem>>
    suspend fun updateQuantity(cartId: Long, quantity: Int): Flow<UpdateQuantityResponse>
    suspend fun deleteCartItem(cartId: Long): Flow<Boolean>
    suspend fun addCartItem(userId: Long, quantity: Int, size: String, variationId: Long): Flow<CreateCartResponse>
    suspend fun createOrder(createOrderRequest: CreateOrderRequest): Flow<CreateOrderResponse>
    suspend fun createVNPayLink(amount: Long): Flow<String>
    suspend fun getOrderHistory(userId: Long): Flow<List<OrderHistory>>
    suspend fun updateOrderStatus(orderId: Long, status: String): Flow<String>
}
