package com.gianghv.uniqlo.data.source.remote.api

import com.gianghv.uniqlo.constant.BASE_URL
import com.gianghv.uniqlo.coredata.BaseResponse
import com.gianghv.uniqlo.data.source.remote.request.LoginRequest
import com.gianghv.uniqlo.data.source.remote.response.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Parameters

class UserApi(private val httpClient: HttpClient) {
    suspend fun exampleCreateOrGetUser(): BaseResponse<String>
    = httpClient.post(ApiEndPoint.EXAMPLE_END_POINT).body()

    suspend fun login(email: String, password: String): BaseResponse<LoginResponse> = httpClient.post(BASE_URL + ApiEndPoint.LOGIN_END_POINT){
        setBody(FormDataContent(Parameters.build {
            append("email", email)
            append("password", password)
            append("role", "user")
        }))
    }.body()
}
