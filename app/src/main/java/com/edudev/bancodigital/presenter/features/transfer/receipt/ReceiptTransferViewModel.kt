package com.edudev.bancodigital.presenter.features.transfer.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.data.model.Deposit
import com.edudev.bancodigital.data.model.Transfer
import com.edudev.bancodigital.domain.profile.GetProfileUseCase
import com.edudev.bancodigital.domain.transfer.GetTransferUseCase
import com.edudev.bancodigital.domain.transfer.SaveTransferUseCase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ReceiptTransferViewModel @Inject constructor(
    private val getTransferUseCase: GetTransferUseCase,
    private val getProfileUseCase: GetProfileUseCase
): ViewModel(){

    fun getTransfer(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val transfer = getTransferUseCase.invoke(id)

            emit(StateView.Sucess(transfer))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getProfile(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val user = getProfileUseCase.invoke(id)

            emit(StateView.Sucess(user))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}