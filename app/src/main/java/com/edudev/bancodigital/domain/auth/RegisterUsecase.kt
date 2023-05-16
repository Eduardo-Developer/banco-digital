package com.edudev.bancodigital.domain.auth

import com.edudev.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl

class RegisterUsecase (
    private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
        ) {
    suspend operator fun invoke(nome: String, email: String, phone: String, password: String) {
        return authFirebaseDataSourceImpl.register(nome, email, phone, password)
    }
}