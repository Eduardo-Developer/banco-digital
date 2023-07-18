package com.edudev.bancodigital.di

import com.edudev.bancodigital.data.repository.auth.AuthFirebaseDataSource
import com.edudev.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import com.edudev.bancodigital.data.repository.deposit.DepositDataSource
import com.edudev.bancodigital.data.repository.deposit.DepositDataSourceImpl
import com.edudev.bancodigital.data.repository.transaction.TransactionDataSource
import com.edudev.bancodigital.data.repository.transaction.TransactionDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindsAuthDataSource(
        authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
    ): AuthFirebaseDataSource

    @Binds
    abstract fun bindsDepositDataSource(
        depositDataSourceImpl: DepositDataSourceImpl
    ): DepositDataSource

    @Binds
    abstract fun bindsTransactionDataSource(
        transactionDataSourceImpl: TransactionDataSourceImpl
    ): TransactionDataSource
}