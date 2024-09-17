package com.gianghv.uniqlo.data.source.remote

import com.gianghv.uniqlo.coredata.BaseDataSource
import com.gianghv.uniqlo.coredata.Result
import com.gianghv.uniqlo.data.source.remote.api.UserApi
import com.gianghv.uniqlo.data.source.remote.response.LoginResponse

interface UserRemoteSource {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun signUp(email: String, password: String, name: String, dateOfBirth: String, gender: String): Result<Boolean>
}

class UserRemoteSourceImpl(private val userApi: UserApi) : UserRemoteSource, BaseDataSource() {
    override suspend fun login(email: String, password: String) = result {
        userApi.login(email, password)
    }

    override suspend fun signUp(email: String, password: String, name: String, dateOfBirth: String, gender: String): Result<Boolean> = returnIfSuccess {
        userApi.signUp(email, password, name, dateOfBirth, gender)
    }
}
