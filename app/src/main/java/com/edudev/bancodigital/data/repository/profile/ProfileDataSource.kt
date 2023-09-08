package com.edudev.bancodigital.data.repository.profile

import com.edudev.bancodigital.data.model.User

interface ProfileDataSource {

    suspend fun saveProfile(user: User)
    suspend fun getProfile(id: String): User
    suspend fun getProfileList(): List<User>
    suspend fun saveImage(imageProfile: String): String
}