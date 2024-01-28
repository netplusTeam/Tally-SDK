package com.netplus.tallyqrgeneratorui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netplus.coremechanism.utils.TallSecurityUtil
import com.netplus.tallyqrgeneratorui.R
import com.netplus.tallyqrgeneratorui.adapters.TokenizedCardsAdapter

class AllTokenizedCardsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tokenizedCardsAdapter: TokenizedCardsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_all_tokenized_cards, container, false)

        initViews(rootView)
        initRecycler()
        return rootView
    }

    private fun initViews(rootView: View) {
        recyclerView = rootView.findViewById(R.id.tokenized_cards_recycle)
    }

    private fun initRecycler() {
        val tokenizedCardsData = TallSecurityUtil.retrieveData(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        tokenizedCardsAdapter = TokenizedCardsAdapter( tokenizedCardsData ?: emptyList())
        recyclerView.apply {
            adapter = tokenizedCardsAdapter
        }
    }
}