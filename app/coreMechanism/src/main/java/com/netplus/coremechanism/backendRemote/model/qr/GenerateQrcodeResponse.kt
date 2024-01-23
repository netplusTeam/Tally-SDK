package com.netplus.coremechanism.backendRemote.model.qr

data class GenerateQrcodeResponse(
    val qr_code_id: String?,
    val data: String?,
    val success: Boolean,
    val card_scheme: String?,
    val issuing_bank: String?,
    val date: String? = null
)