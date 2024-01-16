package com.netplus.coremechanism.mvvm

import com.netplus.coremechanism.backendRemote.TallyEndpoints
import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrPayload
import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrcodeResponse
import com.netplus.coremechanism.backendRemote.model.login.LoginPayload
import com.netplus.coremechanism.backendRemote.model.login.LoginResponse
import com.netplus.coremechanism.backendRemote.responseManager.ApiResponseHandler
import com.netplus.coremechanism.backendRemote.responseManager.ErrorMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository class responsible for handling Tally API requests related to authentication and QR code generation.
 *
 * @property tallyEndpoints An instance of the [TallyEndpoints] interface for making API requests.
 */
class TallyRepository(private val tallyEndpoints: TallyEndpoints) {

    // Error mapper instance to handle error responses.
    var errorMapper: ErrorMapper = ErrorMapper()

    /**
     * Logs in a user with the provided email and password.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @param callback The callback for handling the API response.
     */
    fun login(email: String, password: String, callback: ApiResponseHandler.Callback<LoginResponse>) {
        val apiResponseHandler = ApiResponseHandler<LoginResponse>()
        val loginPayload = LoginPayload(email, password)
        tallyEndpoints.login(loginPayload).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    apiResponseHandler.handleResponse(response.body(), null, callback)
                } else {
                    val error = errorMapper.parseErrorMessage(response.errorBody())
                    apiResponseHandler.handleResponse(null, error?.message, callback)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                apiResponseHandler.handleResponse(null, t.message, callback)
            }
        })
    }

    /**
     * Generates a QR code with the provided information.
     *
     * @param userId The user's ID.
     * @param cardCvv The CVV of the card.
     * @param cardExpiry The expiry date of the card.
     * @param cardNumber The card number.
     * @param cardScheme The card scheme.
     * @param email The user's email.
     * @param fullName The user's full name.
     * @param issuingBank The issuing bank of the card.
     * @param mobilePhone The user's mobile phone number.
     * @param appCode The application code.
     * @param callback The callback for handling the API response.
     */
    fun generateQrcode(
        userId: Int,
        cardCvv: String,
        cardExpiry: String,
        cardNumber: String,
        cardScheme: String,
        email: String,
        fullName: String,
        issuingBank: String,
        mobilePhone: String,
        appCode: String,
        callback: ApiResponseHandler.Callback<GenerateQrcodeResponse>
    ) {
        val apiResponseHandler = ApiResponseHandler<GenerateQrcodeResponse>()
        val generateQrPayload = GenerateQrPayload(
            userId,
            cardCvv,
            cardExpiry,
            cardNumber,
            cardScheme,
            email,
            fullName,
            issuingBank,
            mobilePhone,
            appCode
        )
        tallyEndpoints.generateQrcode(generateQrPayload)
            .enqueue(object : Callback<GenerateQrcodeResponse> {
                override fun onResponse(
                    call: Call<GenerateQrcodeResponse>,
                    response: Response<GenerateQrcodeResponse>
                ) {
                    if (response.isSuccessful) {
                        apiResponseHandler.handleResponse(response.body(), null, callback)
                    } else {
                        val error = errorMapper.parseErrorMessage(response.errorBody())
                        apiResponseHandler.handleResponse(null, error?.message, callback)
                    }
                }

                override fun onFailure(call: Call<GenerateQrcodeResponse>, t: Throwable) {
                    apiResponseHandler.handleResponse(null, t.message, callback)
                }
            })
    }
}