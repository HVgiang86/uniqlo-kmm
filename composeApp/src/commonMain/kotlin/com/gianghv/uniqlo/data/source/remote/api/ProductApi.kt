package com.gianghv.uniqlo.data.source.remote.api

import com.gianghv.uniqlo.constant.BASE_URL
import com.gianghv.uniqlo.coredata.BaseResponse
import com.gianghv.uniqlo.data.source.remote.api.ApiEndPoint.PRODUCT_END_POINT
import com.gianghv.uniqlo.domain.Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ProductApi(private val httpClient: HttpClient) {
    suspend fun getAllProduct(): BaseResponse<List<Product>> = httpClient.get(BASE_URL + PRODUCT_END_POINT).body()
}
