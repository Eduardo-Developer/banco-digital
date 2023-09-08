package com.edudev.bancodigital.data.enum

enum class TransactionOperation {
    DEPOSIT,
    RECHARGE,
    TRANSFER;

    companion object{
        fun getOperation(operation: TransactionOperation): String {
            return when (operation) {
                DEPOSIT -> {
                    "DEPÓSITO"
                }

                TRANSFER -> {
                    "TRANSFERÊNCIA"
                }

                RECHARGE -> {
                    "RECARGA DE TELEFONE"
                }
            }
        }
    }
}