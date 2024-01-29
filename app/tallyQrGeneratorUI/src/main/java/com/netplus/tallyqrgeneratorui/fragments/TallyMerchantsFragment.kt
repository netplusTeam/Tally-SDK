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
import com.netplus.coremechanism.backendRemote.model.merchants.AllMerchantResponse
import com.netplus.coremechanism.backendRemote.model.merchants.Merchant
import com.netplus.coremechanism.utils.TallyQrcodeGenerator
import com.netplus.coremechanism.utils.TallyResponseCallback
import com.netplus.coremechanism.utils.gone
import com.netplus.coremechanism.utils.visible
import com.netplus.tallyqrgeneratorui.R
import com.netplus.tallyqrgeneratorui.adapters.AllMerchantAdapter

class TallyMerchantsFragment : Fragment(), AllMerchantAdapter.Interaction {

    private lateinit var recyclerView: RecyclerView
    private lateinit var allMerchantAdapter: AllMerchantAdapter
    private val tallyQrcodeGenerator = TallyQrcodeGenerator()
    private lateinit var qrInfoLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_tally_merchants, container, false)

        recyclerView = rootView.findViewById(R.id.merchant_recycler)
        qrInfoLayout = rootView.findViewById(R.id.merchant_info_layout)

        initView()

        return rootView
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        observer()
    }

    private fun observer() {
        tallyQrcodeGenerator.getAllMerchants(
            token = "",
            limit = 1,
            page = 10,
            object : TallyResponseCallback<AllMerchantResponse> {
                override fun success(data: AllMerchantResponse?) {
                    Log.e("MERCHANT", "success: $data")
                    if (data?.data?.isEmpty() == true) {
                        switchViewVisibility(isListEmpty = true)
                    } else {
                        switchViewVisibility(isListEmpty = false)
                        allMerchantAdapter = AllMerchantAdapter(
                            this@TallyMerchantsFragment,
                            data?.data ?: emptyList()
                        )
                        recyclerView.adapter = allMerchantAdapter
                    }
                }

                override fun failed(message: String?) {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onItemSelected(merchant: Merchant) {

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