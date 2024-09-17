package com.gianghv.uniqlo.data.source.remote

import com.gianghv.uniqlo.coredata.BaseDataSource
import com.gianghv.uniqlo.coredata.Result
import com.gianghv.uniqlo.data.source.remote.api.UserApi
import com.gianghv.uniqlo.data.source.remote.response.LoginResponse

interface UserRemoteSource {
    suspend fun login(email: String, password: String): Result<LoginResponse>
}

class UserRemoteSourceImpl(private val userApi: UserApi) : UserRemoteSource, BaseDataSource() {
    override suspend fun login(email: String, password: String) = result {
        userApi.login(email, password)
    }
}
