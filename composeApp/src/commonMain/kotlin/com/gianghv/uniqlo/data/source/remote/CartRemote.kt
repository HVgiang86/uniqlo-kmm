package com.gianghv.uniqlo.data.source.remote

import com.gianghv.uniqlo.coredata.BaseDataSource
import com.gianghv.uniqlo.coredata.Result
import com.gianghv.uniqlo.data.source.remote.api.CartApi
import com.gianghv.uniqlo.data.source.remote.response.CreateCartResponse
import com.gianghv.uniqlo.data.source.remote.response.UpdateQuantityResponse
import com.gianghv.uniqlo.domain.CartItem

interface CartRemote {
    suspend fun getCartItems(userId: Long): Result<List<CartItem>>
    suspend fun updateQuantity(cartId: Long, quantity: Int): Result<UpdateQuantityResponse>
    suspend fun deleteCartItem(cartId: Long): Result<Boolean>
    suspend fun addCartItem(userId: Long, quantity: Int, size: String, variationId: Long): Result<List<CreateCartResponse>>
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
}
