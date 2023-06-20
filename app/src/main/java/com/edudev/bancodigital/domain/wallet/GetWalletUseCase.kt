package com.edudev.bancodigital.domain.wallet

import com.edudev.bancodigital.data.model.Wallet
import com.edudev.bancodigital.data.repository.wallet.WalletDataSourceImpl
import javax.inject.Inject

class GetWalletUseCase @Inject constructor(
    private val walletDataSource: WalletDataSourceImpl
) {
    suspend operator fun invoke() : Wallet {
        return walletDataSource.getWallet()
    }
}