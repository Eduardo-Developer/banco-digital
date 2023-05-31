package com.edudev.bancodigital.presenter.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.data.model.User
import com.edudev.bancodigital.domain.auth.RegisterUsecase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUsecase: RegisterUsecase
) : ViewModel() {
    fun register(name: String, email: String, phone: String, password: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val user = registerUsecase.invoke(name, email, phone, password)
            emit(StateView.Sucess(user))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}