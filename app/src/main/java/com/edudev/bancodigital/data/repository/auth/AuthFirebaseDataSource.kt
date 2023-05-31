package com.edudev.bancodigital.data.repository.auth

import com.edudev.bancodigital.data.model.User

interface AuthFirebaseDataSource {

    suspend fun login(
        email: String,
        password: String
    )

    suspend fun register(
        user: String,
        email: String,
        phone: String,
        password: String
    ): User

    suspend fun recover(
        email: String,
    )
}
