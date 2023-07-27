package com.edudev.bancodigital.domain.recharge

import com.edudev.bancodigital.data.model.Recharge
import com.edudev.bancodigital.data.repository.recharge.RechargeDataSourceImpl
import javax.inject.Inject

class SaveRechargeUseCase @Inject constructor(
    private val rechargeDataSourceImpl: RechargeDataSourceImpl
){
    suspend operator fun invoke(recharge: Recharge): Recharge {
        return rechargeDataSourceImpl.saveRecharge(recharge)
    }
}