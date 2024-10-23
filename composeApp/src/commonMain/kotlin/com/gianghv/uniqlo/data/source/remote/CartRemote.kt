package com.gianghv.uniqlo.data.source.remote

import com.gianghv.uniqlo.coredata.BaseDataSource
import com.gianghv.uniqlo.coredata.Result
import com.gianghv.uniqlo.data.source.remote.api.CartApi
import com.gianghv.uniqlo.data.source.remote.request.CreateOrderRequest
import com.gianghv.uniqlo.data.source.remote.response.CreateCartResponse
import com.gianghv.uniqlo.data.source.remote.response.CreateOrderResponse
import com.gianghv.uniqlo.data.source.remote.response.UpdateQuantityResponse
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.domain.OrderHistory

interface CartRemote {
    suspend fun getCartItems(userId: Long): Result<List<CartItem>>
    suspend fun updateQuantity(cartId: Long, quantity: Int): Result<UpdateQuantityResponse>
    suspend fun deleteCartItem(cartId: Long): Result<Boolean>
    suspend fun addCartItem(userId: Long, quantity: Int, size: String, variationId: Long): Result<List<CreateCartResponse>>
    suspend fun createOrder(createOrderRequest: CreateOrderRequest): Result<List<CreateOrderResponse>>
    suspend fun createVNPayLink(amount: Long): Result<String>
    suspend fun getOrderHistory(userId: Long): Result<List<OrderHistory>>
    suspend fun updateOrderStatus(orderId: Long, status: String): Result<String>
}

class CartRemoteImpl(private val cartApi: CartApi) : CartRemote, BaseDataSource() {
    override suspend fun getCartItems(userId: Long): Result<List<CartItem>> = result {
        cartApi.getCartItems(userId)
    }

    override suspend fun updateQuantity(cartId: Long, quantity: Int): Result<UpdateQuantityResponse> = result {
        cartApi.updateQuantity(cartId, quantity)
    }

    override suspend fun deleteCartItem(cartId: Long): Result<Boolean> = returnIf(condition = {
        it.affected == 1
    }) {
        cartApi.deleteCartItem(cartId)
    }

    override suspend fun addCartItem(userId: Long, quantity: Int, size: String, variationId: Long): Result<List<CreateCartResponse>> = result {
        cartApi.addCartItem(userId, quantity, size, variationId)
    }

    override suspend fun createOrder(createOrderRequest: CreateOrderRequest): Result<List<CreateOrderResponse>> = result {
        cartApi.createOrder(createOrderRequest)
    }

    override suspend fun createVNPayLink(amount: Long): Result<String> = result {
        cartApi.createVnPayLink(amount)
    }

    override suspend fun getOrderHistory(userId: Long): Result<List<OrderHistory>> = result {
        cartApi.getOrderByUserId(userId)
    }

    override suspend fun updateOrderStatus(orderId: Long, status: String): Result<String> {
        return result {
            cartApi.updateOrderStatus(orderId, status)
        }
    }
}
