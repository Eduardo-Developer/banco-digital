package com.edudev.bancodigital.data.repository.profile

import com.edudev.bancodigital.data.model.User

interface ProfileDataSource {

    suspend fun saveProfile(user: User)
    suspend fun getProfile(): User
    suspend fun getProfileList(): List<User>
}