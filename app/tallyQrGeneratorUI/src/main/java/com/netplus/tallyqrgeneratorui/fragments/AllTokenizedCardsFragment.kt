package com.netplus.tallyqrgeneratorui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netplus.coremechanism.backendRemote.model.qr.GenerateQrcodeResponse
import com.netplus.coremechanism.utils.TallSecurityUtil
import com.netplus.coremechanism.utils.gone
import com.netplus.coremechanism.utils.launchActivity
import com.netplus.coremechanism.utils.visible
import com.netplus.tallyqrgeneratorui.R
import com.netplus.tallyqrgeneratorui.activities.SingleQrTransactionsActivity
import com.netplus.tallyqrgeneratorui.adapters.TokenizedCardsAdapter

class AllTokenizedCardsFragment : Fragment(), TokenizedCardsAdapter.Interaction {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tokenizedCardsAdapter: TokenizedCardsAdapter
    private lateinit var qrInfoLayout: LinearLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_all_tokenized_cards, container, false)

        initViews(rootView)

        return rootView
    }

    private fun initViews(rootView: View) {
        recyclerView = rootView.findViewById(R.id.tokenized_cards_recycle)
        qrInfoLayout = rootView.findViewById(R.id.token_info_layout)
    }

    override fun onResume() {
        super.onResume()
        initRecycler()
    }

    private fun initRecycler() {
        val tokenizedCardsData = TallSecurityUtil.retrieveData(requireContext())
        if (tokenizedCardsData?.isEmpty() == true) {
            switchViewVisibility(true)
        } else switchViewVisibility(false)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        tokenizedCardsAdapter = TokenizedCardsAdapter(this, tokenizedCardsData ?: emptyList())
        recyclerView.apply {
            adapter = tokenizedCardsAdapter
        }
    }

    override fun onItemSelected(
        absoluteAdapterPosition: Int,
        generateQrcodeResponse: GenerateQrcodeResponse
    ) {
        requireActivity().launchActivity<SingleQrTransactionsActivity> {
            putExtra("qrc ode_id", generateQrcodeResponse.qr_code_id)
        }
    }

    private fun switchViewVisibility(isListEmpty: Boolean) {
        if (isListEmpty) {
            recyclerView.gone()
            qrInfoLayout.visible()
        } else {
            qrInfoLayout.gone()
            recyclerView.visible()
        }
    }
}