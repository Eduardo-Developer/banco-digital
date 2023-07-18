package com.edudev.bancodigital.data.model

import com.edudev.bancodigital.data.enum.TransactionOperation
import com.edudev.bancodigital.data.enum.TransactionType
import com.google.firebase.database.FirebaseDatabase

data class Transaction(
    var id: String = "",
    val operation: TransactionOperation? = null,
    val date: Long = 0,
    val amount: Float = 0f,
    var type: TransactionType? = null
)