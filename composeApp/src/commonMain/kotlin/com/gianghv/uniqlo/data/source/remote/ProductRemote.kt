package com.gianghv.uniqlo.data.source.remote

import com.gianghv.uniqlo.coredata.BaseDataSource
import com.gianghv.uniqlo.coredata.Result
import com.gianghv.uniqlo.data.source.remote.api.ProductApi
import com.gianghv.uniqlo.data.source.remote.request.CreateEvaluationRequest
import com.gianghv.uniqlo.data.source.remote.response.EvaluationResponse
import com.gianghv.uniqlo.data.source.remote.response.RecommendProductResponse
import com.gianghv.uniqlo.data.source.remote.response.SimilarityProductResponse
import com.gianghv.uniqlo.domain.Product

interface ProductDataSource {
    suspend fun getAllProduct(): Result<List<Product>>
    suspend fun getProductDetail(productId: String): Result<Product>
    suspend fun getUserRecommendProduct(userId: Long): Result<List<RecommendProductResponse>>
    suspend fun getSimilarityProduct(productId: Long): Result<List<SimilarityProductResponse>>
    suspend fun getProductEvaluation(productId: Long): Result<List<EvaluationResponse>>
    suspend fun postProductEvaluation(productId: Long, userId: Long, star: Double, content: String): Result<Boolean>
}

class ProductDataSourceImpl(private val productApi: ProductApi) : ProductDataSource, BaseDataSource() {
    override suspend fun getAllProduct(): Result<List<Product>> = result {
        productApi.getAllProduct()
    }

    override suspend fun getProductDetail(productId: String): Result<Product> = result {
        productApi.getProductDetail(productId)
    }

    override suspend fun getUserRecommendProduct(userId: Long): Result<List<RecommendProductResponse>> = result {
        productApi.getUserRecommendProduct(userId)
    }

    override suspend fun getSimilarityProduct(productId: Long): Result<List<SimilarityProductResponse>> = result {
        productApi.getSimilarityProduct(productId)
    }

    override suspend fun getProductEvaluation(productId: Long): Result<List<EvaluationResponse>> {
        return result {
            productApi.getProductEvaluation(productId)
        }
    }

    override suspend fun postProductEvaluation(productId: Long, userId: Long, star: Double, content: String): Result<Boolean> {
        return returnIfSuccess {
            productApi.postProductEvaluation(productId, userId, star, content)
        }
    }
}
