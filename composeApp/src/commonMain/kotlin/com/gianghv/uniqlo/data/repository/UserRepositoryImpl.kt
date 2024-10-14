package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.coredata.BaseRepository
import com.gianghv.uniqlo.coredata.mapDataOnSuccess
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.source.remote.UserRemoteSource
import com.gianghv.uniqlo.domain.User
import com.gianghv.uniqlo.maper.ProfileMapper
import com.gianghv.uniqlo.maper.UserMapper
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val userRemoteSource: UserRemoteSource, private val userMapper: UserMapper, private val profileMapper: ProfileMapper) :
    UserRepository, BaseRepository() {
    override suspend fun login(email: String, password: String) = flowContext {
        userRemoteSource.login(email, password).mapDataOnSuccess {
            userMapper.map(it)
        }
    }

    override suspend fun signUp(email: String, password: String, name: String, dateOfBirth: String, gender: String): Flow<Boolean> = flowContext {
        userRemoteSource.signUp(email, password, name, dateOfBirth, gender)
    }

    override suspend fun getWishlist(userId: Long): Flow<List<Long>> = flowContext {
        userRemoteSource.getWishlist(userId).mapDataOnSuccess { response ->
            response.map { it.productId }
        }
    }

    override suspend fun changeWishlist(userId: Long, productId: Long, isFavorite: Boolean): Flow<Boolean> = flowContext {
        if (isFavorite) userRemoteSource.addWishlist(userId, productId)
        else userRemoteSource.removeWishList(userId, productId)
    }

    override suspend fun getMyProfile(userId: Long): Flow<User> = flowContext {
        userRemoteSource.getMyProfile(userId).mapDataOnSuccess {
            profileMapper.map(it)
        }
    }
}
