package com.gianghv.uniqlo.data.source.remote

import com.gianghv.uniqlo.coredata.BaseDataSource
import com.gianghv.uniqlo.coredata.Result
import com.gianghv.uniqlo.data.source.remote.api.UserApi
import com.gianghv.uniqlo.data.source.remote.response.GetWishlistResponse
import com.gianghv.uniqlo.data.source.remote.response.LoginResponse
import com.gianghv.uniqlo.data.source.remote.response.ProfileResponse

interface UserRemoteSource {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun signUp(email: String, password: String, name: String, dateOfBirth: String, gender: String): Result<Boolean>
    suspend fun getWishlist(userId: Long): Result<List<GetWishlistResponse>>
    suspend fun addWishlist(userId: Long, productId: Long): Result<Boolean>
    suspend fun removeWishList(userId: Long, productId: Long): Result<Boolean>
    suspend fun getMyProfile(userId: Long): Result<ProfileResponse>
}

class UserRemoteSourceImpl(private val userApi: UserApi) : UserRemoteSource, BaseDataSource() {
    override suspend fun login(email: String, password: String) = result {
        userApi.login(email, password)
    }

    override suspend fun signUp(email: String, password: String, name: String, dateOfBirth: String, gender: String): Result<Boolean> = returnIfSuccess {
        userApi.signUp(email, password, name, dateOfBirth, gender)
    }

    override suspend fun getWishlist(userId: Long): Result<List<GetWishlistResponse>> = result {
        userApi.getWishlist(userId)
    }

    override suspend fun addWishlist(userId: Long, productId: Long): Result<Boolean> = returnIfSuccess {
        userApi.addWishlist(userId, productId)
    }

    override suspend fun removeWishList(userId: Long, productId: Long): Result<Boolean> = returnIfSuccess {
        userApi.removeWishlist(userId, productId)
    }

    override suspend fun getMyProfile(userId: Long): Result<ProfileResponse> = result {
        userApi.getMyProfile(userId)
    }
}
