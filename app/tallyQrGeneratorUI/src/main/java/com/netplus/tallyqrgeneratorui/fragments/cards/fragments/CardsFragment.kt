package com.netplus.tallyqrgeneratorui.fragments.cards.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrcodeResponse
import com.netplus.coremechanism.utils.CustomProgressDialog
import com.netplus.coremechanism.utils.TallyQrcodeGenerator
import com.netplus.coremechanism.utils.TallyResponseCallback
import com.netplus.coremechanism.utils.formatCardNumber
import com.netplus.coremechanism.utils.getCardType
import com.netplus.coremechanism.utils.isValidCardNumber
import com.netplus.coremechanism.utils.isValidExpiryDate
import com.netplus.coremechanism.utils.listOfCardSchemes
import com.netplus.tallyqrgeneratorui.R


class CardsFragment : Fragment() {

    private val tallyQrcodeGenerator = TallyQrcodeGenerator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_cards, container, false)
        val cardNumber = rootView.findViewById<AppCompatEditText>(R.id.etCardNumber)
        val cardExpiryMonth = rootView.findViewById<AppCompatEditText>(R.id.et_mm)
        val cardExpiryYear = rootView.findViewById<AppCompatEditText>(R.id.et_yy)
        val cardCvv = rootView.findViewById<AppCompatEditText>(R.id.et_cvv)
        val generateQrBtn = rootView.findViewById<AppCompatButton>(R.id.generate_qr_button)

        val customProgressDialog = CustomProgressDialog(requireContext())
        initView(
            cardCvv,
            cardNumber,
            cardExpiryMonth,
            cardExpiryYear,
            generateQrBtn,
            customProgressDialog
        )
        return rootView
    }

    private fun initView(
        cardCvv: AppCompatEditText,
        cardNumberEdt: AppCompatEditText,
        cardExpiryMonth: AppCompatEditText,
        cardExpiryYear: AppCompatEditText,
        generateQrBtn: AppCompatButton,
        customProgressDialog: CustomProgressDialog
    ) {
        cardNumberEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val cardNumber = s.toString().replace(" - ", "")
                val formattedCardNumber = formatCardNumber(cardNumber)
                cardNumberEdt.removeTextChangedListener(this)
                cardNumberEdt.setText(formattedCardNumber)
                cardNumberEdt.setSelection(formattedCardNumber.length)
                cardNumberEdt.addTextChangedListener(this)
            }
        })

        generateQrBtn.setOnClickListener {
            val inputtedCardNumber = cardNumberEdt.text.toString().replace(" - ", "")
            val expiryMonth = cardExpiryMonth.text.toString().toIntOrNull() ?: 0
            val expiryYear = cardExpiryYear.text.toString().toIntOrNull() ?: 0
            validateCardInformation(
                inputtedCardNumber,
                expiryMonth,
                expiryYear,
                cardCvv,
                customProgressDialog
            )
        }
    }

    private fun validateCardInformation(
        inputtedCardNumber: String,
        expiryMonth: Int,
        expiryYear: Int,
        cardCvv: AppCompatEditText,
        customProgressDialog: CustomProgressDialog
    ) {
        val cardType = getCardType(inputtedCardNumber)
        if (isValidExpiryDate(expiryMonth, expiryYear)) {
            if (isValidCardNumber(inputtedCardNumber, cardType)) {
                when (cardType) {
                    listOfCardSchemes[0] -> { /*Show pin bottom_sheet*/
                    }

                    listOfCardSchemes[1] -> {
                        customProgressDialog.show()
                        customProgressDialog.setUpdateText("Generating Qrcode...")
                        generateQrcode(
                            userId = 12,
                            cardCvv = cardCvv.text.toString(),
                            cardExpiry = "$expiryMonth/$expiryYear",
                            cardNumber = inputtedCardNumber,
                            cardScheme = cardType,
                            email = "nicholasanyanwu125@gmail.com",
                            fullName = "Nicholas",
                            issuingBank = "Wema",
                            mobilePhone = "09090909090",
                            appCode = "203",
                            customProgressDialog = customProgressDialog
                        )
                    }

                    listOfCardSchemes[2] -> {}
                    listOfCardSchemes[3] -> {}
                    listOfCardSchemes[4] -> { /*Show pin bottom_sheet*/
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Invalid card information", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(requireContext(), "Invalid card expiry date", Toast.LENGTH_LONG).show()
        }
    }

    private fun generateQrcode(
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
        customProgressDialog: CustomProgressDialog
    ) {
        tallyQrcodeGenerator.generateQrcode(
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
            object : TallyResponseCallback<GenerateQrcodeResponse> {
                override fun success(data: GenerateQrcodeResponse?) {
                    customProgressDialog.setUpdateText("Generating successful...")
                    Handler(Looper.getMainLooper()).postDelayed({
                        customProgressDialog.dismiss()
                    }, 2000)
                }

                override fun failed(message: String?) {
                    customProgressDialog.setUpdateText(message.toString())
                    Handler(Looper.getMainLooper()).postDelayed({
                        customProgressDialog.dismiss()
                    }, 2000)
                }
            })
    }
}