package com.edudev.bancodigital.domain.wallet

import com.edudev.bancodigital.data.model.Wallet
import com.edudev.bancodigital.data.repository.wallet.WalletDataSourceImpl
import javax.inject.Inject

class InitWalletUsecase @Inject constructor(
    private val walletDataSource: WalletDataSourceImpl
) {
    suspend operator fun invoke(wallet: Wallet) {
        return walletDataSource.initWallet(wallet)
    }
}