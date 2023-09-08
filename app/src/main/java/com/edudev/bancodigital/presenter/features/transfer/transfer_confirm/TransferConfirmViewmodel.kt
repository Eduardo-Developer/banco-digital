package com.edudev.bancodigital.presenter.features.transfer.transfer_confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.data.model.Transaction
import com.edudev.bancodigital.data.model.Transfer
import com.edudev.bancodigital.domain.transaction.GetBalanceUseCase
import com.edudev.bancodigital.domain.transaction.SaveTransactionUseCase
import com.edudev.bancodigital.domain.transfer.SaveTransferTransactionUseCase
import com.edudev.bancodigital.domain.transfer.SaveTransferUseCase
import com.edudev.bancodigital.domain.transfer.UpdateTransactionTransferUseCase
import com.edudev.bancodigital.domain.transfer.UpdateTransferUseCase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferConfirmViewmodel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val saveTransferUseCase: SaveTransferUseCase,
    private val updateTransferUseCase: UpdateTransferUseCase,
    private val saveTransferTransactionUseCase: SaveTransferTransactionUseCase,
    private val updateTransactionTransferUseCase: UpdateTransactionTransferUseCase
) : ViewModel() {
    fun getBalance() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val balance = getBalanceUseCase.invoke()
            emit(StateView.Sucess(balance))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveTransferUseCase.invoke(transfer)

            emit(StateView.Sucess(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun updateTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            updateTransferUseCase.invoke(transfer)

            emit(StateView.Sucess(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun updateTransactionTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            updateTransactionTransferUseCase.invoke(transfer)

            emit(StateView.Sucess(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveTransferTransaction(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveTransferTransactionUseCase.invoke(transfer)

            emit(StateView.Sucess(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}