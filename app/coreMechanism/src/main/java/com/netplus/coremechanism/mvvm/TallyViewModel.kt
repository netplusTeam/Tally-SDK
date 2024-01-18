package com.netplus.coremechanism.mvvm

import androidx.lifecycle.ViewModel
import com.netplus.coremechanism.backendRemote.model.login.LoginResponse
import com.netplus.coremechanism.backendRemote.model.merchants.AllMerchantResponse
import com.netplus.coremechanism.backendRemote.model.merchants.MerchantResponse
import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrcodeResponse
import com.netplus.coremechanism.backendRemote.model.transactions.TransactionResponse
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

    /**
     * Gets all transactions with the provided information
     *
     * @param qrcodeId The QrcodeId from which transactions will be queried
     * @param page The number of pages to return
     * @param pageSize The size of transactions to be returned in a page
     * @param callback The callback for handling API response
     */
    fun getTransactions(
        qrcodeId: String,
        page: Int,
        pageSize: Int,
        callback: ApiResponseHandler.Callback<TransactionResponse>
    ) = tallyRepository.getTransactions(qrcodeId, page, pageSize, callback)

    /**
     * Get selected merchant with the provided information
     *
     * @param token The authorization token
     * @param search The name of merchant to search
     * @param limit The limit
     * @param page The number of pages to return
     * @param callback The callback for handling API response
     */
    fun getMerchant(
        token: String,
        search: String,
        limit: Int,
        page: Int,
        callback: ApiResponseHandler.Callback<MerchantResponse>
    ) = tallyRepository.getMerchant(token, search, limit, page, callback)

    /**
     * Get all merchants
     *
     * @param token The authorization token
     * @param limit The page limit
     * @param page The page number
     * @param callback The callback for handling API response
     */
    fun getAllMerchant(
        token: String,
        limit: Int,
        page: Int,
        callback: ApiResponseHandler.Callback<AllMerchantResponse>
    ) = tallyRepository.getAllMerchant(token, limit, page, callback)
}