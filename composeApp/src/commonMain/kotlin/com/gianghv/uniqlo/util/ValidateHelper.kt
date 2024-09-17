package com.gianghv.uniqlo.util


object ValidateHelper {
    const val PASSWORD_MIN_LENGTH = 6

    fun validateEmail(email: String): String? {
        return if (email.isBlank() || !EMAIL_ADDRESS_PATTERN.matches(email)) {
            "Email is invalid"
        } else {
            null
        }
    }

    fun validatePassword(password: String): String? {
        return if (password.isBlank() || password.length < PASSWORD_MIN_LENGTH) {
            "Password is invalid"
        } else {
            null
        }
    }

    fun validateNotEmpty(value: String): String? {
        return if (value.isBlank()) {
            "This field is required"
        } else {
            null
        }
    }

//    fun validatePassword(password: String): String? {
//        return if (password.isBlank() || !PASSWORD_PATTERN.matches(password)) {
//            "Password is invalid"
//        } else {
//            null
//        }
//    }
}

val EMAIL_ADDRESS_PATTERN = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
val PASSWORD_PATTERN = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{6,}$")
