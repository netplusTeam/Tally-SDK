package com.netplus.coremechanism.backendRemote.model.qr.retreive

data class GetTokenizedCardsResponse(
    val data: List<Data>,
    val status: String
)