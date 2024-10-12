package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.coredata.BaseRepository
import com.gianghv.uniqlo.coredata.mapDataOnSuccess
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.source.remote.ProductDataSource
import com.gianghv.uniqlo.domain.Product
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(private val productRemote: ProductDataSource) : ProductRepository, BaseRepository() {
    override suspend fun getAllProduct(): Flow<List<Product>> = flowContext {
        productRemote.getAllProduct()
    }

    override suspend fun getRecommendProduct(): Flow<List<Product>> = flowContext {
        productRemote.getRecommendProduct()
    }

    override suspend fun getProductDetail(productId: Long): Flow<Product> = flowContext {
        productRemote.getProductDetail(productId.toString())
    }

    override suspend fun searchProduct(query: String): Flow<List<Product>> = flowContext {
        productRemote.getAllProduct().mapDataOnSuccess {
            it.filter { product -> product.name?.lowercase()?.contains(query.lowercase()) == true }
        }
    }
}
