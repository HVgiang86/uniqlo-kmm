package com.gianghv.uniqlo.data

import com.gianghv.uniqlo.domain.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): Flow<User>
    suspend fun signUp(email: String, password: String, name: String, dateOfBirth: String, gender: String): Flow<Boolean>
}
