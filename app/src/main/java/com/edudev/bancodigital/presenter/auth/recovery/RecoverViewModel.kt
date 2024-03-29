package com.edudev.bancodigital.presenter.auth.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.domain.auth.RecoverUseCase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RecoverViewModel @Inject constructor(
    private val recoverUsecase: RecoverUseCase
) : ViewModel() {
    fun recover(email: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val user = recoverUsecase.invoke(email)
            emit(StateView.Sucess(null))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}