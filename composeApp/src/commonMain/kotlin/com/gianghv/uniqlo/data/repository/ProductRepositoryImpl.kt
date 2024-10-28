package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.coredata.BaseRepository
import com.gianghv.uniqlo.coredata.mapDataOnSuccess
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.source.remote.ProductDataSource
import com.gianghv.uniqlo.domain.Evaluation
import com.gianghv.uniqlo.domain.Product
import com.gianghv.uniqlo.domain.User
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(private val productRemote: ProductDataSource) : ProductRepository, BaseRepository() {
    override suspend fun getAllProduct(): Flow<List<Product>> = flowContext {
        productRemote.getAllProduct()
    }

    override suspend fun getProductDetail(productId: Long): Flow<Product> = flowContext {
        productRemote.getProductDetail(productId.toString())
    }

    override suspend fun searchProduct(query: String): Flow<List<Product>> = flowContext {
        productRemote.getAllProduct().mapDataOnSuccess {
            it.filter { product -> product.name?.lowercase()?.contains(query.lowercase()) == true }
        }
    }

    override suspend fun getSimilarProduct(productId: Long): Flow<List<Long>> = flowContext {
        productRemote.getSimilarityProduct(productId).mapDataOnSuccess {
            it.map { product -> product.productId }
        }
    }

    override suspend fun getUserRecommendProduct(userId: Long): Flow<List<Long>> = flowContext {
        productRemote.getUserRecommendProduct(userId).mapDataOnSuccess {
            it.map { product -> product.productId }
        }
    }

    override suspend fun getProductEvaluation(productId: Long): Flow<List<Evaluation>> {
        return flowContext {
            productRemote.getProductEvaluation(productId).mapDataOnSuccess {
                it.map { evaluation ->
                    val account = User(
                        id = evaluation.account?.id ?: 0,
                        name = evaluation.account?.name ?: "",
                        imagePath = evaluation.account?.imagePath ?: "",
                        email = "",
                        phone = ""
                    )

                    Evaluation(
                        id = evaluation.id, star = evaluation.star, content = evaluation.review?.content ?: "", account = account
                    )
                }
            }
        }
    }

    override suspend fun postProductEvaluation(productId: Long, userId: Long, star: Double, content: String): Flow<Boolean> {
        return flowContext {
            productRemote.postProductEvaluation(productId, userId, star, content)
        }
    }
}
