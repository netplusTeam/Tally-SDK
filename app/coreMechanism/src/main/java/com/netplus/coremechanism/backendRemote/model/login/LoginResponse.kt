package com.netplus.coremechanism.backendRemote.model.login

data class LoginResponse(
    val message: String,
    val refreshToken: String,
    val success: Boolean,
    val token: String
)