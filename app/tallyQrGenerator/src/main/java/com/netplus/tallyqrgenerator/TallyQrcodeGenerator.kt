package com.netplus.tallyqrgenerator

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.netplus.coremechanism.backendRemote.model.login.LoginResponse
import com.netplus.coremechanism.backendRemote.model.merchants.AllMerchantResponse
import com.netplus.coremechanism.backendRemote.model.merchants.MerchantResponse
import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrcodeResponse
import com.netplus.coremechanism.backendRemote.model.transactions.TransactionResponse
import com.netplus.coremechanism.backendRemote.responseManager.ApiResponseHandler
import com.netplus.coremechanism.internet.handler.InternetConfigViewModel
import com.netplus.coremechanism.mvvm.TallyViewModel
import com.netplus.coremechanism.utils.AppPreferences
import com.netplus.coremechanism.utils.TallyResponseCallback
import org.koin.java.KoinJavaComponent.getKoin

/**
 * Activity class responsible for authenticating the bank and generating QR codes.
 */
class TallyQrcodeGenerator : AppCompatActivity() {

    companion object {
        // Lazy initialization of TallyViewModel and InternetConfigViewModel
        private val tallyViewModel: TallyViewModel by lazy { getKoin().get<TallyViewModel>() }
        private val internetConfigViewModel: InternetConfigViewModel by lazy { getKoin().get<InternetConfigViewModel>() }
    }

    /**
     * Authenticates the bank with the provided email and password.
     *
     * @param email The user's email for authentication.
     * @param password The user's password for authentication.
     * @param context The context used for operations like displaying Toast messages.
     * @param callback The callback for handling the authentication response.
     */
    fun authenticateBank(
        email: String,
        password: String,
        callback: TallyResponseCallback<LoginResponse>
    ) {
        // Observe network state using InternetConfigViewModel
        internetConfigViewModel.networkState(this)?.observe(this) {
            // Check if the device is connected to the internet
            when {
                // Initiate user login through TallyViewModel
                true -> tallyViewModel.login(
                    email,
                    password,
                    object : ApiResponseHandler.Callback<LoginResponse> {
                        override fun onSuccess(data: LoginResponse?) {
                            // Handle successful login response
                            callback.success(data)
                            // Save the authentication token to preferences
                            AppPreferences.getInstance(this@TallyQrcodeGenerator)
                                .setStringValue(data?.token ?: "")
                        }

                        override fun onError(errorMessage: String?) {
                            // Handle login error
                            callback.failed(errorMessage)
                        }
                    })

                // Display a Toast message if the device is not connected
                false -> Toast.makeText(
                    this,
                    "This device is not connected to Internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Generates a QR code with the provided information.
     *
     * @param context The context used for operations like displaying Toast messages.
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
     * @param callback The callback for handling the QR code generation response.
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
        callback: TallyResponseCallback<GenerateQrcodeResponse>
    ) {
        // Observe network state using InternetConfigViewModel
        internetConfigViewModel.networkState(this)?.observe(this) {
            // Check if the device is connected to the internet
            when {
                // Initiate QR code generation through TallyViewModel
                true -> tallyViewModel.generateQrcode(
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
                    object : ApiResponseHandler.Callback<GenerateQrcodeResponse> {
                        // Handle successful QR code generation response
                        override fun onSuccess(data: GenerateQrcodeResponse?) {
                            callback.success(data)
                        }

                        // Handle QR code generation error
                        override fun onError(errorMessage: String?) {
                            callback.failed(errorMessage)
                        }
                    }
                )

                // Display a Toast message if the device is not connected
                else -> Toast.makeText(
                    this,
                    "This device is not connected to Internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

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
        callback: TallyResponseCallback<TransactionResponse>
    ) {
        internetConfigViewModel.networkState(this)?.observe(this) {
            when {
                true -> tallyViewModel.getTransactions(
                    qrcodeId,
                    page,
                    pageSize,
                    object : ApiResponseHandler.Callback<TransactionResponse> {
                        override fun onSuccess(data: TransactionResponse?) {
                            callback.success(data)
                        }

                        // Handle error when getting transactions
                        override fun onError(errorMessage: String?) {
                            callback.failed(errorMessage)
                        }
                    }
                )

                // Display a Toast message if the device is not connected
                else -> Toast.makeText(
                    this,
                    "This device is not connected to Internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

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
        callback: TallyResponseCallback<MerchantResponse>
    ) {
        internetConfigViewModel.networkState(this)?.observe(this) {
            when {
                true -> tallyViewModel.getMerchant(
                    token,
                    search,
                    limit,
                    page,
                    object : ApiResponseHandler.Callback<MerchantResponse> {
                        override fun onSuccess(data: MerchantResponse?) {
                            callback.success(data)
                        }

                        override fun onError(errorMessage: String?) {
                            callback.failed(errorMessage)
                        }
                    }
                )

                false -> Toast.makeText(
                    this,
                    "This device is not connected to Internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Get all merchants
     *
     * @param token The authorization token
     * @param limit The page limit
     * @param page The page number
     * @param callback The callback for handling API response
     */
    fun getAllMerchants(
        token: String,
        limit: Int,
        page: Int,
        callback: TallyResponseCallback<AllMerchantResponse>
    ) {
        internetConfigViewModel.networkState(this)?.observe(this) {
            when {
                true -> tallyViewModel.getAllMerchant(
                    token,
                    limit,
                    page,
                    object : ApiResponseHandler.Callback<AllMerchantResponse> {
                        override fun onSuccess(data: AllMerchantResponse?) {
                            callback.success(data)
                        }

                        override fun onError(errorMessage: String?) {
                            callback.failed(errorMessage)
                        }
                    }
                )

                false -> Toast.makeText(
                    this,
                    "This device is not connected to Internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}