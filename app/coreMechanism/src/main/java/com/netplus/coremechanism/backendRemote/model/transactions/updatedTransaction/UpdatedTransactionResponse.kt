package com.netplus.coremechanism.backendRemote.model.transactions.updatedTransaction

data class UpdatedTransactionResponse(
    val data: Data,
    val message: String,
    val status: String
)