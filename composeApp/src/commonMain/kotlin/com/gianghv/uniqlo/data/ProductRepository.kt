package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.domain.Evaluation
import com.gianghv.uniqlo.domain.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProduct(): Flow<List<Product>>
    suspend fun getProductDetail(productId: Long): Flow<Product>
    suspend fun searchProduct(query: String): Flow<List<Product>>
    suspend fun getSimilarProduct(productId: Long): Flow<List<Long>>
    suspend fun getUserRecommendProduct(userId: Long): Flow<List<Long>>
    suspend fun getProductEvaluation(productId: Long): Flow<List<Evaluation>>
    suspend fun postProductEvaluation(productId: Long, userId: Long, star: Double, content: String): Flow<Boolean>
}
