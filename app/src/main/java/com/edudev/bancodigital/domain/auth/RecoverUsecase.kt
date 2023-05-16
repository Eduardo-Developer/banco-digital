package com.edudev.bancodigital.domain.auth

import com.edudev.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl

class RecoverUsecase (
    private val authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
        ) {
    suspend operator fun invoke(email: String) {
        return authFirebaseDataSourceImpl.recover(email)
    }
}