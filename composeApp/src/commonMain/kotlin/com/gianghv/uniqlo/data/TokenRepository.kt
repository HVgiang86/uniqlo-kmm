package com.gianghv.uniqlo.data

interface TokenRepository {
    fun getToken(): String?
    fun setToken(token: String)
    fun clearToken()
}
