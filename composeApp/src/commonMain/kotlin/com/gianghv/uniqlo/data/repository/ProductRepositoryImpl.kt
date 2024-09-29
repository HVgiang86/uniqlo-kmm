package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.coredata.BaseRepository
import com.gianghv.uniqlo.data.ProductRepository
import com.gianghv.uniqlo.data.source.remote.ProductDataSource
import com.gianghv.uniqlo.domain.Product
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl (private val productRemote: ProductDataSource) : ProductRepository, BaseRepository() {
    override suspend fun getAllProduct(): Flow<List<Product>> = flowContext {
        productRemote.getAllProduct()
    }
}
