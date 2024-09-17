package com.gianghv.uniqlo.data.repository

import com.gianghv.uniqlo.coredata.BaseRepository
import com.gianghv.uniqlo.coredata.mapDataOnSuccess
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.source.remote.UserRemoteSource
import com.gianghv.uniqlo.maper.UserMapper

class UserRepositoryImpl(private val userRemoteSource: UserRemoteSource, private val userMapper: UserMapper) : UserRepository, BaseRepository() {
    override suspend fun login(email: String, password: String) = flowContext {
        userRemoteSource.login(email, password).mapDataOnSuccess {
            userMapper.map(it)
        }
    }
}
