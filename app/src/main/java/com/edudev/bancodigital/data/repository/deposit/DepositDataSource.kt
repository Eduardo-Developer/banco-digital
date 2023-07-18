package com.edudev.bancodigital.data.repository.deposit

import com.edudev.bancodigital.data.model.Deposit

interface DepositDataSource {

    suspend fun saveDeposit(deposit: Deposit): Deposit

    suspend fun getDeposit(id: String): Deposit
}