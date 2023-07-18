package com.edudev.bancodigital.domain.transaction

import com.edudev.bancodigital.data.repository.transaction.TransactionDataSourceImpl
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionDataSourceImpl: TransactionDataSourceImpl
) {
    suspend operator fun  invoke(): List<com.edudev.bancodigital.data.model.Transaction> {
        return transactionDataSourceImpl.getTransactions()
    }
}