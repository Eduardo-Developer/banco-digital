package com.edudev.bancodigital.domain.profile

import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.data.repository.profile.ProfileRepositoryImpl
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val profileRepositoryImpl: ProfileRepositoryImpl
) {
    suspend operator fun invoke(user: User) {
        return profileRepositoryImpl.saveProfile(user)
    }
}