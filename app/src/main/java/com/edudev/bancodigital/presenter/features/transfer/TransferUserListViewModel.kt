package com.edudev.bancodigital.presenter.features.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edudev.bancodigital.domain.profile.GetListProfileUseCase
import com.edudev.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferUserListViewModel @Inject constructor(
    private val listProfileUseCase: GetListProfileUseCase
) : ViewModel() {

    fun getProfileList() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val user = listProfileUseCase.invoke()

            emit(StateView.Sucess(user))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}