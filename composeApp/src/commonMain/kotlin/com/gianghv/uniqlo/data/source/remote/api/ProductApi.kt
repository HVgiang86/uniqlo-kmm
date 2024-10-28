package com.gianghv.uniqlo.data.source.remote.api

import com.gianghv.uniqlo.constant.BASE_URL
import com.gianghv.uniqlo.constant.BASE_URL_V1
import com.gianghv.uniqlo.coredata.BaseResponse
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.data.source.remote.api.ApiEndPoint.PRODUCT_END_POINT
import com.gianghv.uniqlo.data.source.remote.request.CreateEvaluationRequest
import com.gianghv.uniqlo.data.source.remote.response.EvaluationResponse
import com.gianghv.uniqlo.data.source.remote.response.RecommendProductResponse
import com.gianghv.uniqlo.data.source.remote.response.SimilarityProductResponse
import com.gianghv.uniqlo.domain.Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ProductApi(private val httpClient: HttpClient) {
    suspend fun getAllProduct(): BaseResponse<List<Product>> = httpClient.get(BASE_URL + PRODUCT_END_POINT).body()
    suspend fun getProductDetail(productId: String): BaseResponse<Product> = httpClient.get("$BASE_URL$PRODUCT_END_POINT/$productId").body()

    suspend fun getUserRecommendProduct(userId: Long): BaseResponse<List<RecommendProductResponse>> =
        httpClient.get("${WholeApp.RECOMMEND_BASE_URL}/cfrecommend/$userId").body()

    suspend fun getSimilarityProduct(productId: Long): BaseResponse<List<SimilarityProductResponse>> =
        httpClient.get("${WholeApp.RECOMMEND_BASE_URL}/recommend/$productId").body()

    suspend fun getProductEvaluation(productId: Long): BaseResponse<List<EvaluationResponse>> = httpClient.get("${BASE_URL}evaluations/stars/$productId").body()

    suspend fun postProductEvaluation(productId: Long, userId: Long, star: Double, content: String): BaseResponse<CreateEvaluationRequest> =
        httpClient.post("${BASE_URL_V1}evaluations/create") {
            setBody(CreateEvaluationRequest(productId = productId, accountId = userId, star = star, content = content))
        }.body()
}
