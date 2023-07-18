package com.edudev.bancodigital.presenter.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.data.model.Deposit
import com.edudev.bancodigital.domain.deposit.GetDepositUseCase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositReceiptViewModel @Inject constructor(
    private val getDepositUseCase: GetDepositUseCase
) : ViewModel() {

    fun getDeposit(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val deposit = getDepositUseCase.invoke(id)

            emit(StateView.Sucess(deposit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}