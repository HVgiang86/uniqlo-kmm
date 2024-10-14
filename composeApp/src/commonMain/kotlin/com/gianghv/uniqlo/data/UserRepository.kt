package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.domain.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): Flow<User>
    suspend fun signUp(email: String, password: String, name: String, dateOfBirth: String, gender: String): Flow<Boolean>
    suspend fun getWishlist(userId: Long): Flow<List<Long>>
    suspend fun changeWishlist(userId: Long, productId: Long, isFavorite: Boolean): Flow<Boolean>
    suspend fun getMyProfile(userId: Long): Flow<User>
}
