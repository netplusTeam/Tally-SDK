package com.netplus.coremechanism.backendRemote.model.transactions

data class TransactionResponse(
    val data: TransactionsData,
    val message: String,
    val status: String
)