package com.netplus.tallyqrgenerator

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.netplus.coremechanism.backendRemote.model.GenerateQrcodeResponse
import com.netplus.coremechanism.backendRemote.model.login.LoginResponse
import com.netplus.coremechanism.backendRemote.responseManager.ApiResponseHandler
import com.netplus.coremechanism.internet.handler.InternetConfigViewModel
import com.netplus.coremechanism.mvvm.TallyViewModel
import com.netplus.coremechanism.utils.AppPreferences
import com.netplus.coremechanism.utils.GeneratorCallback
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
        context: Context,
        callback: GeneratorCallback<LoginResponse>
    ) {
        // Observe network state using InternetConfigViewModel
        internetConfigViewModel.networkState(context)?.observe(this) {
            // Check if the device is connected to the internet
            when {
                // Initiate user login through TallyViewModel
                true -> tallyViewModel.login(
                    email,
                    password,
                    object : ApiResponseHandler.Callback<LoginResponse> {
                        override fun onSuccess(data: LoginResponse?) {
                            // Handle successful login response
                            callback.onQrcodeGenerateSuccess(data)
                            // Save the authentication token to preferences
                            AppPreferences.getInstance(context)
                                .setStringValue(data?.token ?: "")
                        }

                        override fun onError(errorMessage: String?) {
                            // Handle login error
                            callback.onQrcodeGenerateFailed(errorMessage)
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
        context: Context,
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
        callback: GeneratorCallback<GenerateQrcodeResponse>
    ) {
        // Observe network state using InternetConfigViewModel
        internetConfigViewModel.networkState(context)?.observe(this) {
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
                            callback.onQrcodeGenerateSuccess(data)
                        }

                        // Handle QR code generation error
                        override fun onError(errorMessage: String?) {
                            callback.onQrcodeGenerateFailed(errorMessage)
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
}