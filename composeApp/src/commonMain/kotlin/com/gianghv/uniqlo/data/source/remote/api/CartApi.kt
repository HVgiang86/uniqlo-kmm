package com.gianghv.uniqlo.data.source.remote.api

import com.gianghv.uniqlo.constant.BASE_URL
import com.gianghv.uniqlo.coredata.BaseResponse
import com.gianghv.uniqlo.data.source.remote.api.ApiEndPoint.CART_API_END_POINT
import com.gianghv.uniqlo.data.source.remote.request.CreateCartRequest
import com.gianghv.uniqlo.data.source.remote.request.UpdateQuantityRequest
import com.gianghv.uniqlo.data.source.remote.response.Affected
import com.gianghv.uniqlo.data.source.remote.response.CreateCartResponse
import com.gianghv.uniqlo.data.source.remote.response.UpdateQuantityResponse
import com.gianghv.uniqlo.domain.CartItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class CartApi(private val httpClient: HttpClient) {
    suspend fun getCartItems(userId: Long): BaseResponse<List<CartItem>> = httpClient.get("$BASE_URL$CART_API_END_POINT/$userId").body()
    suspend fun updateQuantity(cartId: Long, quantity: Int): BaseResponse<UpdateQuantityResponse> =
        httpClient.put("$BASE_URL$CART_API_END_POINT/update-quantity") {
            setBody(UpdateQuantityRequest(cartId, quantity))
        }.body()

    suspend fun deleteCartItem(cartId: Long): BaseResponse<Affected> = httpClient.delete("$BASE_URL$CART_API_END_POINT/$cartId").body()
    suspend fun addCartItem(userId: Long, quantity: Int, size: String, variationId: Long): BaseResponse<List<CreateCartResponse>> =
        httpClient.post("$BASE_URL$CART_API_END_POINT/create") {
            setBody(CreateCartRequest(variationId, userId, quantity, size))
        }.body()
}
