package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.domain.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProduct(): Flow<List<Product>>

}
