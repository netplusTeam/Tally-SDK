package com.netplus.tallyqrgeneratorui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netplus.coremechanism.backendRemote.model.qr.EncryptedQrModel
import com.netplus.coremechanism.utils.TallSecurityUtil
import com.netplus.coremechanism.utils.gone
import com.netplus.coremechanism.utils.launchActivity
import com.netplus.coremechanism.utils.visible
import com.netplus.tallyqrgeneratorui.R
import com.netplus.tallyqrgeneratorui.activities.SingleQrTransactionsActivity
import com.netplus.tallyqrgeneratorui.adapters.TokenizedCardsAdapter
import com.netplus.tallyqrgeneratorui.utils.ProgressDialogUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllTokenizedCardsFragment : Fragment(), TokenizedCardsAdapter.Interaction {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tokenizedCardsAdapter: TokenizedCardsAdapter
    private lateinit var qrInfoLayout: LinearLayout
    private val progressDialogUtil by lazy { ProgressDialogUtil(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_all_tokenized_cards, container, false)

        initViews(rootView)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
    }

    private fun initViews(rootView: View) {
        recyclerView = rootView.findViewById(R.id.tokenized_cards_recycle)
        qrInfoLayout = rootView.findViewById(R.id.token_info_layout)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initRecycler() {
        //progressDialogUtil.showProgressDialog("Loading...")

        GlobalScope.launch(Dispatchers.IO) {
            val tokenizedCardsData = TallSecurityUtil.retrieveData(requireContext())
            withContext(Dispatchers.Main) {
                if (tokenizedCardsData?.isEmpty() == true) {
                    switchViewVisibility(true)
                } else switchViewVisibility(false)

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                tokenizedCardsAdapter = TokenizedCardsAdapter(this@AllTokenizedCardsFragment, tokenizedCardsData ?: emptyList())
                recyclerView.apply {
                    adapter = tokenizedCardsAdapter
                }
                //progressDialogUtil.dismissProgressDialog()
            }
        }
    }

    override fun onItemSelected(
        absoluteAdapterPosition: Int,
        encryptedQrModel: EncryptedQrModel
    ) {
        requireActivity().launchActivity<SingleQrTransactionsActivity> {
            putExtra("qrcode_id", encryptedQrModel.qrcodeId)
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

    override fun onPause() {
        super.onPause()
        progressDialogUtil.dismissProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialogUtil.dismissProgressDialog()
    }
}