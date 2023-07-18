package com.edudev.bancodigital.domain.auth

import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.data.repository.auth.AuthFirebaseDataSource
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authFirebaseDataSource: AuthFirebaseDataSource
        ) {
    suspend operator fun invoke(name: String, email: String, phone: String, password: String) : User {
        return authFirebaseDataSource.register(name, email, phone, password)
    }
}