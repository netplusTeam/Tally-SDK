package com.netplus.coremechanism.backendRemote.model.transactions

data class Transaction(
    val accountType: Any?,
    val acquiringInstCode: Any?,
    val additionalAmount: Any?,
    val agentName: Any?,
    val aid: Any?,
    val amount: Int?,
    val appCryptogram: Any?,
    val authCode: Any?,
    val cardExpiry: Any?,
    val cardHolder: Any?,
    val cardLabel: Any?,
    val dateCreated: String?,
    val localDate: Any?,
    val localTime_: Any?,
    val maskedPan: String?,
    val merchantId: String?,
    val merchantName: Any?,
    val operatorId: Any?,
    val originalForwardingInstCode: Any?,
    val otherAmount: Any?,
    val otherId: String?,
    val partnerId: String?,
    val provider: String?,
    val remark: Any?,
    val responseCode: Any?,
    val responseDE55: Any?,
    val responseMessage: String?,
    val rrn: String?,
    val source: String?,
    val stan: Any?,
    val terminalId: String?,
    val transactionTime: String?,
    val transactionTimeInMillis: Any?,
    val transactionType: Any?,
    val transmissionDateTime: String?,
    val tsi: Any?,
    val tvr: Any?,
    val webHookResponseMessage: Any?
)