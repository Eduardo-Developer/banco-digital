package com.edudev.bancodigital.data.model

import com.google.firebase.database.Exclude

data class User(
    val id: String? = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    @get:Exclude
    val password: String
)
