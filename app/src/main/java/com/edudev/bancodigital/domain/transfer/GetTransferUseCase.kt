package com.edudev.bancodigital.domain.transfer

import com.edudev.bancodigital.data.model.Transfer
import com.edudev.bancodigital.data.repository.transfer.TransferDataSourceImpl
import javax.inject.Inject

class GetTransferUseCase @Inject constructor(
    private val transferDataSourceImpl: TransferDataSourceImpl
) {
    suspend operator fun invoke(id: String): Transfer {
        return transferDataSourceImpl.getTransfer(id)
    }
}