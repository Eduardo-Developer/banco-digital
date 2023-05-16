package com.edudev.bancodigital.domain.auth

import com.edudev.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl

class LoginUsecase (
    private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
        ) {
    suspend operator fun invoke(email: String, password: String) {
        return authFirebaseDataSourceImpl.login(email, password)
    }
}