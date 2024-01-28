package com.netplus.tallyqrgeneratorui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.netplus.tallyqrgeneratorui.R
import com.netplus.tallyqrgeneratorui.adapters.TabPagerAdapter
import com.netplus.tallyqrgeneratorui.fragments.cards.fragments.CardsFragment
import com.netplus.tallyqrgeneratorui.fragments.cards.fragments.RecentTokenizedCardFragment


class CardTokenizationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_card_tokenization, container, false)
        val tab = rootView.findViewById<TabLayout>(R.id.cards_tokenization_tab)
        val pager = rootView.findViewById<ViewPager>(R.id.viewPager)

        initTabs(tab, pager)

        return rootView
    }

    private fun initTabs(tab: TabLayout, pager: ViewPager) {
        val tabFragments = ArrayList<Fragment>().apply {
            add(CardsFragment())
            add(RecentTokenizedCardFragment())
        }

        val tabTitles = ArrayList<String>().apply {
            add("Card")
            add("Recent Tokenized Card")
        }

        pager.adapter =
            TabPagerAdapter(childFragmentManager, tabFragments, tabTitles)
        pager.setPadding(0, 0, 0, 0)
        pager.pageMargin = 0
        tab.setupWithViewPager(pager)
    }
}