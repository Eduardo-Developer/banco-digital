package com.edudev.bancodigital.domain.profile

import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.data.repository.profile.ProfileDataSourceImpl
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileDataSourceImpl: ProfileDataSourceImpl
) {
    suspend operator fun invoke() : User {
        return profileDataSourceImpl.getProfile()
    }
}