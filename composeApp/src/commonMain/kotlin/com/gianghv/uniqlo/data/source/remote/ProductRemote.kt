package com.gianghv.uniqlo.data.source.remote

import com.gianghv.uniqlo.coredata.BaseDataSource
import com.gianghv.uniqlo.coredata.Result
import com.gianghv.uniqlo.data.source.remote.api.ProductApi
import com.gianghv.uniqlo.domain.Product

interface ProductDataSource {
    suspend fun getAllProduct(): Result<List<Product>>
}

class ProductDataSourceImpl(private val productApi: ProductApi) : ProductDataSource, BaseDataSource() {
    override suspend fun getAllProduct(): Result<List<Product>> = result {
        productApi.getAllProduct()
    }
}
