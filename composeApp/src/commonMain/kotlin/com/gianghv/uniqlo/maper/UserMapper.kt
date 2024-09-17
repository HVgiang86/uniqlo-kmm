package com.gianghv.uniqlo.maper

import com.gianghv.uniqlo.data.source.remote.response.LoginResponse
import com.gianghv.uniqlo.domain.User

class UserMapper : DataMapper<LoginResponse, User>() {
    override fun map(data: LoginResponse): User {
        return User(
            id = data.id,
            name = data.name,
            email = data.email,
            phone = data.phone,
            imagePath = data.imagePath,
            gender = data.gender,
            role = data.role,
            active = data.active,
            passwordReset = data.passwordReset,
            birthday = data.birthday
        )
    }
}

