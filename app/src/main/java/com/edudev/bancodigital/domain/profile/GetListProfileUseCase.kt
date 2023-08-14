package com.edudev.bancodigital.domain.profile

import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.data.repository.profile.ProfileDataSourceImpl
import javax.inject.Inject

class GetListProfileUseCase @Inject constructor(
    private val profileDataSourceImpl: ProfileDataSourceImpl
) {
    suspend operator fun invoke() : List<User> {
        return profileDataSourceImpl.getProfileList()
    }
}