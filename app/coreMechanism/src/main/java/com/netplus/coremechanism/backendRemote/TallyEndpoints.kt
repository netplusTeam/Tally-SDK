package com.netplus.coremechanism.backendRemote

import com.netplus.coremechanism.backendRemote.model.login.LoginPayload
import com.netplus.coremechanism.backendRemote.model.login.LoginResponse
import com.netplus.coremechanism.backendRemote.model.merchants.AllMerchantResponse
import com.netplus.coremechanism.backendRemote.model.merchants.MerchantResponse
import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrPayload
import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrcodeResponse
import com.netplus.coremechanism.backendRemote.model.transactions.TransactionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface defining API endpoints for Tally authentication and QR code generation.
 */
interface TallyEndpoints {

    /**
     * Sends a [POST] request to authenticate the user.
     *
     * @param loginPayload The payload containing user credentials.
     * @return A [Call] object wrapping the [LoginResponse].
     */
    @POST("auth/login")
    fun login(@Body loginPayload: LoginPayload): Call<LoginResponse>

    /**
     * Sends a [POST] request to generate a QR code.
     *
     * @param generateQrPayload The payload containing data for QR code generation.
     * @return A [Call] object wrapping the [GenerateQrcodeResponse].
     */
    @POST("qr")
    fun generateQrcode(@Body generateQrPayload: GenerateQrPayload): Call<GenerateQrcodeResponse>

    /**
     * Sends a [GET] request to get all transactions performed from the tokenized/generated Qrcode
     *
     * @param qr_code_id
     * @param page
     * @param pageSize
     * @return A [Call] object wrapping the [TransactionResponse].
     */
    @GET("qrcode_transactions/{qr_code_id}")
    fun getTransactions(
        @Path("qr_code_id") qr_code_id: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Call<TransactionResponse>

    /**
     * Sends a [GET] request to user type searched merchant
     *
     * @param token
     * @param search
     * @param limit
     * @param page
     */
    @GET("user/search-partner-user")
    fun getMerchant(
        @Header("token") token: String,
        @Query("search") search: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Call<MerchantResponse>

    /**
     * Sends [GET] request to get all merchants
     *
     * @param token
     * @param limit
     * @param page
     */
    @GET("user/get-partner-user")
    fun getAllMerchant(
        @Header("token") token: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ) : Call<AllMerchantResponse>
}