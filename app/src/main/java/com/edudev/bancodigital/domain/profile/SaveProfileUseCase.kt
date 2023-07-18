package com.edudev.bancodigital.domain.profile

import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.data.repository.profile.ProfileDataSourceImpl
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val profileDataSourceImpl: ProfileDataSourceImpl
) {
    suspend operator fun invoke(user: User) {
        return profileDataSourceImpl.saveProfile(user)
    }
}