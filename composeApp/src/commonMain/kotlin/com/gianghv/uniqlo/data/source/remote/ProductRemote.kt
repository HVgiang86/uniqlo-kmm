package com.gianghv.uniqlo.data.source.remote

import com.gianghv.uniqlo.coredata.BaseDataSource
import com.gianghv.uniqlo.coredata.BaseResponse
import com.gianghv.uniqlo.coredata.Result
import com.gianghv.uniqlo.data.FakeData
import com.gianghv.uniqlo.data.source.remote.api.ProductApi
import com.gianghv.uniqlo.data.source.remote.response.RecommendProductResponse
import com.gianghv.uniqlo.data.source.remote.response.SimilarityProductResponse
import com.gianghv.uniqlo.domain.Product

interface ProductDataSource {
    suspend fun getAllProduct(): Result<List<Product>>
    suspend fun getProductDetail(productId: String): Result<Product>
    suspend fun getRecommendProduct(): Result<List<Product>>
    suspend fun getUserRecommendProduct(userId: Long): Result<List<RecommendProductResponse>>
    suspend fun getSimilarityProduct(productId: Long): Result<List<SimilarityProductResponse>>
}

class ProductDataSourceImpl(private val productApi: ProductApi) : ProductDataSource, BaseDataSource() {
    override suspend fun getAllProduct(): Result<List<Product>> = result {
        productApi.getAllProduct()
    }
//    override suspend fun getAllProduct(): Result<List<Product>> = result {
//        BaseResponse(
//            code = 200, message = "success", data = FakeData.products
//        )
//    }

    override suspend fun getProductDetail(productId: String): Result<Product> = result {
        productApi.getProductDetail(productId)
    }

    override suspend fun getRecommendProduct(): Result<List<Product>> = result {
        BaseResponse(
            code = 200, message = "Success", data = FakeData.products.takeLast(4)
        )
    }

    override suspend fun getUserRecommendProduct(userId: Long): Result<List<RecommendProductResponse>> = result {
        productApi.getUserRecommendProduct(userId)
    }

    override suspend fun getSimilarityProduct(productId: Long): Result<List<SimilarityProductResponse>> = result {
        productApi.getSimilarityProduct(productId)
    }
}
