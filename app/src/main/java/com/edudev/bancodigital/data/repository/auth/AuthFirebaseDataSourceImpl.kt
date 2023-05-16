package com.edudev.bancodigital.data.repository.auth

import com.google.firebase.database.FirebaseDatabase
import java.lang.reflect.Constructor
import javax.inject.Inject

class AuthFirebaseDataSourceImpl @Inject constructor(
    firebaseDatabase: FirebaseDatabase
): AuthFirebaseDataSource {

    override suspend fun login(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun register(nome: String, email: String, phone: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun recover(email: String) {
        TODO("Not yet implemented")
    }

}