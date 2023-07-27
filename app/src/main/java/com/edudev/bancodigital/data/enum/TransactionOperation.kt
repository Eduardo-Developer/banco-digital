package com.edudev.bancodigital.data.enum

enum class TransactionOperation {
    DEPOSIT,
    RECHARGE;

    companion object{
        fun getOperation(operation: TransactionOperation): String {
            return when (operation) {
                DEPOSIT -> {
                    "DEPÓSITO"
                }

                RECHARGE -> {
                    "RECARGA DE TELEFONE"
                }
            }
        }
    }
}