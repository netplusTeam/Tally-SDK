package com.netplus.tallyqrgeneratorui.fragments.cards.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.netplus.coremechanism.utils.TallyAppPreferences
import com.netplus.coremechanism.utils.TallyCustomProgressDialog
import com.netplus.coremechanism.utils.decodeBase64ToBitmap
import com.netplus.coremechanism.utils.gone
import com.netplus.coremechanism.utils.saveImageToGallery
import com.netplus.coremechanism.utils.visible
import com.netplus.tallyqrgeneratorui.R


class RecentTokenizedCardFragment : Fragment() {

    private val tallyCustomProgressDialog by lazy { TallyCustomProgressDialog(requireContext()) }
    private lateinit var bankNameAndScheme: TextView
    private lateinit var dateGenerated: TextView
    private lateinit var qrImage: ImageView
    private lateinit var saveQr: AppCompatButton
    private lateinit var qrInfoLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_recent_tokenized_cards, container, false)

        bankNameAndScheme = rootView.findViewById(R.id.card_and_bank_scheme)
        dateGenerated = rootView.findViewById(R.id.date_created)
        qrImage = rootView.findViewById(R.id.tokenized_card_image)
        saveQr = rootView.findViewById(R.id.save_qr_on_device_btn)
        qrInfoLayout = rootView.findViewById(R.id.token_info_layout)

        initViews()
        clickEvents()

        return rootView
    }

    private fun initViews() {
        val bankName = TallyAppPreferences.getInstance(requireContext())
            .getStringValue(TallyAppPreferences.CARD_AND_BANK_SCHEME)
        val date = TallyAppPreferences.getInstance(requireContext())
            .getStringValue(TallyAppPreferences.DATE_GENERATED)
        val base64Image =
            TallyAppPreferences.getInstance(requireContext())
                .getStringValue(TallyAppPreferences.QRCODE_IMAGE)
        val bitmap =
            decodeBase64ToBitmap(base64Image?.substringAfter("data:image/png;base64,").toString())

        bankNameAndScheme.text = bankName
        dateGenerated.text = date
        if (bitmap != null) {
            qrImage.setImageBitmap(bitmap)
            switchViewVisibility(true)
        } else {
            // Handle decoding error
            switchViewVisibility(false)
        }
    }

    private fun clickEvents() {
        saveQr.setOnClickListener {
            saveImageToGallery(requireContext(), qrImage)
            Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun switchViewVisibility(isAnyCardRecentlyTokenized: Boolean) {
        if (isAnyCardRecentlyTokenized) {
            qrInfoLayout.gone()
            qrImage.visible()
            bankNameAndScheme.visible()
            dateGenerated.visible()
            saveQr.visible()
        } else {
            qrImage.gone()
            bankNameAndScheme.gone()
            dateGenerated.gone()
            saveQr.gone()
            qrInfoLayout.visible()
        }
    }
}