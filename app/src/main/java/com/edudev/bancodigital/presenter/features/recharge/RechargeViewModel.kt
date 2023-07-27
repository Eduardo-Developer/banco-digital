package com.edudev.bancodigital.presenter.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.data.model.Recharge
import com.edudev.bancodigital.data.model.Transaction
import com.edudev.bancodigital.domain.recharge.SaveRechargeUseCase
import com.edudev.bancodigital.domain.transaction.SaveTransactionUseCase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val rechargeUseCase: SaveRechargeUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase
): ViewModel() {

    fun saveRecharge(recharge: Recharge) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val result = rechargeUseCase.invoke(recharge)

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