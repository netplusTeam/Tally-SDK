package com.netplus.coremechanism.mvvm

import androidx.lifecycle.ViewModel
import com.netplus.coremechanism.backendRemote.model.GenerateQrcodeResponse
import com.netplus.coremechanism.backendRemote.model.login.LoginResponse
import com.netplus.coremechanism.backendRemote.responseManager.ApiResponseHandler

/**
 * ViewModel class for handling Tally-related operations, such as user login and QR code generation.
 *
 * @property tallyRepository An instance of [TallyRepository] for interacting with Tally API.
 */
class TallyViewModel(private val tallyRepository: TallyRepository) : ViewModel() {

    /**
     * Initiates the user login process.
     *
     * @param email The user's email.
     * @param password The user's password.
     * @param callback The callback for handling the API response.
     */
    fun login(
        email: String, password: String, callback: ApiResponseHandler.Callback<LoginResponse>
    ) = tallyRepository.login(email, password, callback)

    /**
     * Initiates the QR code generation process.
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
    ) = tallyRepository.generateQrcode(
        userId,
        cardCvv,
        cardExpiry,
        cardNumber,
        cardScheme,
        email,
        fullName,
        issuingBank,
        mobilePhone,
        appCode,
        callback
    )
}
