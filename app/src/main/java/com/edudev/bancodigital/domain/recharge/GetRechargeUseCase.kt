package com.edudev.bancodigital.domain.recharge

import com.edudev.bancodigital.data.model.Recharge
import com.edudev.bancodigital.data.repository.recharge.RechargeDataSourceImpl
import javax.inject.Inject

class GetRechargeUseCase @Inject constructor(
    private val rechargeDataSourceImpl: RechargeDataSourceImpl
) {
    suspend operator fun invoke(id : String) : Recharge {
        return rechargeDataSourceImpl.getRecharge(id)
    }
}