package com.edudev.bancodigital.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.domain.profile.GetProfileUseCase
import com.edudev.bancodigital.domain.transaction.GetTransactionsUseCase
import com.edudev.bancodigital.domain.wallet.GetWalletUseCase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import java.security.PrivateKey
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWalletUseCase: GetWalletUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {
    fun getWallet() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val wallet = getWalletUseCase.invoke()
            emit(StateView.Sucess(wallet))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getTransactions() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val transactions  = getTransactionsUseCase.invoke()
            emit(StateView.Sucess(transactions))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getProfile() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val user = getProfileUseCase.invoke()

            emit(StateView.Sucess(user))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}