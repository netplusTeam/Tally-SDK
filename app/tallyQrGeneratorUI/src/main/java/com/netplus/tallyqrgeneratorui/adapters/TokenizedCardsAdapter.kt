package com.netplus.tallyqrgeneratorui.adapters

import android.annotation.SuppressLint
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrcodeResponse
import com.netplus.coremechanism.utils.decodeBase64ToBitmap
import com.netplus.coremechanism.utils.gone
import com.netplus.coremechanism.utils.saveImageToGallery
import com.netplus.coremechanism.utils.visible
import com.netplus.tallyqrgeneratorui.R

class TokenizedCardsAdapter(
    private val interaction: Interaction? = null,
    private val generateQrcodeResponse: List<GenerateQrcodeResponse>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TokenizedCardsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.all_tokenized_card_items, parent, false), interaction)
    }

    override fun getItemCount() = generateQrcodeResponse.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TokenizedCardsViewHolder -> {
                holder.bind(generateQrcodeResponse[position], position)
            }
        }
    }

    class TokenizedCardsViewHolder(
        itemView: View,
        private val interaction: Interaction?
    ): RecyclerView.ViewHolder(itemView) {

        private var smallImage = itemView.findViewById<ImageView>(R.id.small_card_image_view)
        private var tokenizedCardImage = itemView.findViewById<ImageView>(R.id.tokenized_card_image)
        private var dropDownIcon = itemView.findViewById<ImageView>(R.id.drop_down_icon)
        private var saveQrIcon = itemView.findViewById<ImageView>(R.id.save_qr_icon)
        private var pushUpIcon = itemView.findViewById<ImageView>(R.id.push_up_icon)
        private var bankName = itemView.findViewById<TextView>(R.id.bank_name)
        private var bankAndSchemeName = itemView.findViewById<TextView>(R.id.card_and_bank_scheme)
        private var dateCreated = itemView.findViewById<TextView>(R.id.date_created)
        private var viewTransactions = itemView.findViewById<AppCompatButton>(R.id.view_transaction_btn)
        private var topConstraints = itemView.findViewById<ConstraintLayout>(R.id.top_constraint)
        private var bottomConstraint = itemView.findViewById<ConstraintLayout>(R.id.bottm_constraints)
        @SuppressLint("SetTextI18n")
        fun bind(generateQrcodeResponse: GenerateQrcodeResponse, position: Int) {

            val qrBitmap = decodeBase64ToBitmap(generateQrcodeResponse.data?.substringAfter("data:image/png;base64,").toString())
            smallImage.setImageBitmap(qrBitmap)
            tokenizedCardImage.setImageBitmap(qrBitmap)
            bankName.text = generateQrcodeResponse.issuing_bank
            bankAndSchemeName.text = "${generateQrcodeResponse.issuing_bank} ${generateQrcodeResponse.card_scheme}"
            dateCreated.text = generateQrcodeResponse.date

            dropDownIcon.setOnClickListener {
                TransitionManager.beginDelayedTransition(bottomConstraint)
                if (bottomConstraint.visibility == View.GONE) {
                    bottomConstraint.visible()
                    topConstraints.gone()
                }
            }

            pushUpIcon.setOnClickListener {
                TransitionManager.beginDelayedTransition(topConstraints)
                if (topConstraints.visibility == View.GONE) {
                    topConstraints.visible()
                    bottomConstraint.gone()
                }
            }

            saveQrIcon.setOnClickListener {
                saveImageToGallery(itemView.context, tokenizedCardImage)
                Toast.makeText(itemView.context, "Saved successfully", Toast.LENGTH_SHORT).show()
            }

            viewTransactions.setOnClickListener {
                interaction?.onItemSelected(position, generateQrcodeResponse)
            }
        }
    }

    interface Interaction{
        fun onItemSelected(
            absoluteAdapterPosition: Int,
            generateQrcodeResponse: GenerateQrcodeResponse
        )
    }
}