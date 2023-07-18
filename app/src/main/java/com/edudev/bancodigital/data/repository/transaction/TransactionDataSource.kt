package com.edudev.bancodigital.data.repository.transaction

import com.edudev.bancodigital.data.model.Transaction

interface TransactionDataSource {
    suspend fun saveTransaction(transaction: Transaction)
    suspend fun getTransactions(): List<Transaction>
}