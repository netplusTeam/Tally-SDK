package com.netplus.coremechanism.backendRemote

import com.netplus.coremechanism.backendRemote.model.GenerateQrPayload
import com.netplus.coremechanism.backendRemote.model.GenerateQrcodeResponse
import com.netplus.coremechanism.backendRemote.model.login.LoginPayload
import com.netplus.coremechanism.backendRemote.model.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interface defining API endpoints for Tally authentication and QR code generation.
 */
interface TallyEndpoints {

    /**
     * Sends a POST request to authenticate the user.
     *
     * @param loginPayload The payload containing user credentials.
     * @return A [Call] object wrapping the [LoginResponse].
     */
    @POST("auth/login")
    fun login(@Body loginPayload: LoginPayload): Call<LoginResponse>

    /**
     * Sends a POST request to generate a QR code.
     *
     * @param generateQrPayload The payload containing data for QR code generation.
     * @return A [Call] object wrapping the [GenerateQrcodeResponse].
     */
    @POST("qr")
    fun generateQrcode(@Body generateQrPayload: GenerateQrPayload): Call<GenerateQrcodeResponse>
}