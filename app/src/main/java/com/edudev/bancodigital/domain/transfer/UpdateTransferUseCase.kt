package com.edudev.bancodigital.domain.transfer

import com.edudev.bancodigital.data.model.Transfer
import com.edudev.bancodigital.data.repository.transfer.TransferDataSourceImpl
import javax.inject.Inject

class UpdateTransferUseCase @Inject constructor(
    private val transferDataSourceImpl: TransferDataSourceImpl
) {
    suspend operator fun invoke(transfer: Transfer) {
        transferDataSourceImpl.updateTransfer(transfer)
    }
}