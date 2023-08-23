package com.edudev.bancodigital.domain.profile

import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.data.repository.profile.ProfileDataSourceImpl
import javax.inject.Inject

class SaveImageProfileUseCase @Inject constructor(
    private val profileDataSourceImpl: ProfileDataSourceImpl
) {
    suspend operator fun invoke(imageProfile: String): String {
        return profileDataSourceImpl.saveImage(imageProfile)
    }
}