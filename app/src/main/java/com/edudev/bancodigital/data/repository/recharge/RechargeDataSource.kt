package com.edudev.bancodigital.data.repository.recharge

import com.edudev.bancodigital.data.model.Recharge

interface RechargeDataSource {

    suspend fun saveRecharge(recharge: Recharge): Recharge

    suspend fun getRecharge(id: String): Recharge
}
