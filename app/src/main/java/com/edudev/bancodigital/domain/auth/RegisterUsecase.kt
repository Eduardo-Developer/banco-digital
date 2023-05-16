package com.edudev.bancodigital.domain.auth

import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.data.repository.auth.AuthFirebaseDataSource
import com.edudev.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject

class RegisterUsecase @Inject constructor(
    private val authFirebaseDataSource: AuthFirebaseDataSource
        ) {
    suspend operator fun invoke(user: User) : User {
        return authFirebaseDataSource.register(user)
    }
}