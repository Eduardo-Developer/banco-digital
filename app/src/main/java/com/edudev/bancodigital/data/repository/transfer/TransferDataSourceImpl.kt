package com.edudev.bancodigital.data.repository.transfer

import com.edudev.bancodigital.data.enum.TransactionOperation
import com.edudev.bancodigital.data.enum.TransactionType
import com.edudev.bancodigital.data.model.Transaction
import com.edudev.bancodigital.data.model.Transfer
import com.edudev.bancodigital.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransferDataSourceImpl @Inject constructor(
    database: FirebaseDatabase
) : TransferDataSource {

    private val transferReference = database.reference
        .child("transfer")

    private val transactionReference = database.reference
        .child("transaction")


    override suspend fun saveTransfer(transfer: Transfer) {
        suspendCoroutine { continuation ->
            transferReference
                .child(transfer.idUserSent)
                .child(transfer.id)
                .setValue(transfer).addOnCompleteListener { taskUserSent ->
                    if (taskUserSent.isSuccessful) {
                        transferReference.child(transfer.idUserReceived)
                            .child(transfer.id)
                            .setValue(transfer).addOnCompleteListener { taskUserReceived ->
                                if (taskUserReceived.isSuccessful) {
                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    taskUserReceived.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }
                    } else {
                        taskUserSent.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun updateTransfer(transfer: Transfer) {
        suspendCoroutine { continuation ->
            transferReference
                .child(transfer.idUserSent)
                .child(transfer.id)
                .child("date")
                .setValue(ServerValue.TIMESTAMP)
                .addOnCompleteListener { taskUpdateSent ->
                    if (taskUpdateSent.isSuccessful) {
                        transferReference
                            .child(transfer.idUserReceived)
                            .child(transfer.id)
                            .child("date")
                            .setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener { taskUpdateReceived ->
                                if (taskUpdateReceived.isSuccessful) {
                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    taskUpdateSent.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }
                    } else {
                        taskUpdateSent.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }

    }

    override suspend fun getTransfer(id: String): Transfer {
        return suspendCoroutine { continuation ->
            transferReference
                .child(FirebaseHelper.getUserId())
                .child(id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val transfer = snapshot.getValue(Transfer::class.java)
                        transfer?.let {
                            continuation.resumeWith(Result.success(transfer))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }

                })
        }
    }

    override suspend fun saveTransferTransaction(transfer: Transfer) {
        return suspendCoroutine { continuation ->

            val transactionUserSent = Transaction(
                id = transfer.id,
                operation = TransactionOperation.TRANSFER,
                date = transfer.date,
                amount = transfer.amount,
                type = TransactionType.CASH_OUT
            )

            val transactionUserReceived = Transaction(
                id = transfer.id,
                operation = TransactionOperation.TRANSFER,
                date = transfer.date,
                amount = transfer.amount,
                type = TransactionType.CASH_IN
            )

            transactionReference
                .child(transfer.idUserSent)
                .child(transfer.id)
                .setValue(transactionUserSent).addOnCompleteListener { taskUserSent ->
                    if (taskUserSent.isSuccessful) {
                        transactionReference
                            .child(transfer.idUserReceived)
                            .child(transfer.id)
                            .setValue(transactionUserReceived)
                            .addOnCompleteListener { taskUserReceived ->
                                if (taskUserReceived.isSuccessful) {
                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    taskUserReceived.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }
                    } else {
                        taskUserSent.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun updateTransferTransaction(transfer: Transfer) {
        suspendCoroutine { continuation ->
            transactionReference
                .child(transfer.idUserSent)
                .child(transfer.id)
                .child("date")
                .setValue(ServerValue.TIMESTAMP)
                .addOnCompleteListener { taskUpdateSent ->
                    if (taskUpdateSent.isSuccessful) {
                        transactionReference
                            .child(transfer.idUserReceived)
                            .child(transfer.id)
                            .child("date")
                            .setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener { taskUpdateReceived ->
                                if (taskUpdateReceived.isSuccessful) {
                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    taskUpdateSent.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }
                    } else {
                        taskUpdateSent.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }
}