package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.coredata.BaseRepository
import com.gianghv.uniqlo.coredata.mapDataOnSuccess
import com.gianghv.uniqlo.data.CartRepository
import com.gianghv.uniqlo.data.source.remote.CartRemote
import com.gianghv.uniqlo.data.source.remote.response.CreateCartResponse
import com.gianghv.uniqlo.data.source.remote.response.UpdateQuantityResponse
import com.gianghv.uniqlo.domain.CartItem
import kotlinx.coroutines.flow.Flow

class CartRepositoryImpl(private val cartRemote: CartRemote) : CartRepository, BaseRepository() {
    override suspend fun getCartItems(userId: Long): Flow<List<CartItem>> = flowContext {
        cartRemote.getCartItems(userId).mapDataOnSuccess {
            it.filter { cartItem ->
                cartItem.variation != null
            }
        }
    }

    override suspend fun updateQuantity(cartId: Long, quantity: Int): Flow<UpdateQuantityResponse> = flowContext {
        cartRemote.updateQuantity(cartId, quantity)
    }

    override suspend fun deleteCartItem(cartId: Long): Flow<Boolean> = flowContext {
        cartRemote.deleteCartItem(cartId)
    }

    override suspend fun addCartItem(userId: Long, quantity: Int, size: String, variationId: Long): Flow<CreateCartResponse> = flowContext {
        cartRemote.addCartItem(userId, quantity, size, variationId).mapDataOnSuccess {
            it.first()
        }
    }
}
