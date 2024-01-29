package com.netplus.tallyqrgeneratorui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netplus.coremechanism.backendRemote.model.transactions.Transaction
import com.netplus.coremechanism.utils.TallSecurityUtil
import com.netplus.coremechanism.utils.TallyCustomProgressDialog
import com.netplus.coremechanism.utils.TallyQrcodeGenerator
import com.netplus.coremechanism.utils.TallyResponseCallback
import com.netplus.coremechanism.utils.extractQrCodeIds
import com.netplus.coremechanism.utils.gone
import com.netplus.coremechanism.utils.visible
import com.netplus.tallyqrgeneratorui.R
import com.netplus.tallyqrgeneratorui.adapters.SingleQrTransactionAdapter

class TokenizedCardsTransactionFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var singleQrTransactionAdapter: SingleQrTransactionAdapter
    private val tallyQrcodeGenerator = TallyQrcodeGenerator()
    private lateinit var qrInfoLayout: LinearLayout
    private val tallyCustomProgressDialog by lazy { TallyCustomProgressDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_tokenized_cards_transaction, container, false)

        recyclerView = rootView.findViewById(R.id.all_qr_transaction_recycler)
        qrInfoLayout = rootView.findViewById(R.id.token_info_layout)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return rootView
    }

    override fun onResume() {
        super.onResume()
        observer()
    }

    private fun observer() {
        val tokenizedCardsData = TallSecurityUtil.retrieveData(requireContext())
        val qrcodeIds = extractQrCodeIds(tokenizedCardsData ?: emptyList())
        Log.e("QR", "Data: $qrcodeIds")
        tallyQrcodeGenerator.getTransactions(
            qrcodeIds,
            1,
            10,
            object : TallyResponseCallback<List<Transaction>> {
                override fun success(data: List<Transaction>?) {
                    Log.e("TAG", "success: $data")
                    if (data.isNullOrEmpty()) {
                        switchViewVisibility(true)
                    } else {
                        switchViewVisibility(false)
                        singleQrTransactionAdapter = SingleQrTransactionAdapter(
                            null,
                            data
                        )
                        recyclerView.adapter = singleQrTransactionAdapter
                    }

                }

                override fun failed(message: String?) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun switchViewVisibility(isListEmpty: Boolean) {
        if (isListEmpty) {
            recyclerView.gone()
            qrInfoLayout.visible()
        } else {
            recyclerView.visible()
            qrInfoLayout.gone()
        }
    }
}