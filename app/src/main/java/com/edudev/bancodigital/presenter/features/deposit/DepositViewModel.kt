package com.edudev.bancodigital.presenter.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.data.model.Deposit
import com.edudev.bancodigital.data.model.Transaction
import com.edudev.bancodigital.domain.deposit.SaveDepositUseCase
import com.edudev.bancodigital.domain.transaction.SaveTransactionUseCase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val depositUseCase: SaveDepositUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase
) : ViewModel() {

    fun saveDeposit(deposit: Deposit) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val result = depositUseCase.invoke(deposit)

            emit(StateView.Sucess(result))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveTransaction(transaction: Transaction) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveTransactionUseCase.invoke(transaction)

            emit(StateView.Sucess(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}