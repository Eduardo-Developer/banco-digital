package com.edudev.bancodigital.data.repository.wallet

import com.edudev.bancodigital.data.model.Wallet

interface WalletDataSource {

    suspend fun initWallet(wallet: Wallet)
    suspend fun getWallet(): Wallet
}