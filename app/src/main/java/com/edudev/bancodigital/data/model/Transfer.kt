package com.edudev.bancodigital.data.model

import android.os.Parcelable
import com.google.firebase.database.FirebaseDatabase
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transfer(
    var id: String = "",
    var idUserReceived: String = "",
    var idUserSent: String = "",
    var date: Long = 0,
    val amount: Float = 0f
): Parcelable {
    init {
        this.id = FirebaseDatabase.getInstance().reference.push().key ?: ""
    }
}
