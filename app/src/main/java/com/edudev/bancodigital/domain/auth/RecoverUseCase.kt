package com.edudev.bancodigital.domain.auth

import com.edudev.bancodigital.data.repository.auth.AuthFirebaseDataSource
import javax.inject.Inject

class RecoverUseCase @Inject constructor(
    private val authFirebaseDataSource: AuthFirebaseDataSource
        ) {
    suspend operator fun invoke(email: String) {
        return authFirebaseDataSource.recover(email)
    }
}