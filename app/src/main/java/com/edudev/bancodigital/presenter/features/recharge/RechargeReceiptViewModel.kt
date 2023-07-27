package com.edudev.bancodigital.presenter.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.domain.recharge.GetRechargeUseCase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeReceiptViewModel @Inject constructor(
    private val rechargeUseCase: GetRechargeUseCase
) : ViewModel() {

    fun getRecharge(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val deposit = rechargeUseCase.invoke(id)

            emit(StateView.Sucess(deposit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}