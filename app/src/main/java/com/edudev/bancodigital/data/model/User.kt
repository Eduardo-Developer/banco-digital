package com.edudev.bancodigital.data.model

import com.google.firebase.database.Exclude

data class User(
    var id: String? = "",
    var name: String? = "",
    val email: String? = "",
    var phone: String? = "",
    var image: String = "",
    @get:Exclude
    val password: String? = ""
)


