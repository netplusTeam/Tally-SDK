package com.netplus.tallyqrgeneratorui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.netplus.coremechanism.backendRemote.model.login.LoginResponse
import com.netplus.coremechanism.utils.CustomProgressDialog
import com.netplus.coremechanism.utils.TallyQrcodeGenerator
import com.netplus.coremechanism.utils.TallyResponseCallback
import com.netplus.tallyqrgeneratorui.adapters.TabPagerAdapter
import com.netplus.tallyqrgeneratorui.fragments.TallyMerchantsFragment
import com.netplus.tallyqrgeneratorui.fragments.TokenizedCardsTransactionFragment
import com.netplus.tallyqrgeneratorui.fragments.cards.CardTokenizationFragment

class TallyActivity : AppCompatActivity() {

    //private lateinit var binding: TallActivityBinding

    private val tallyQrcodeGenerator = TallyQrcodeGenerator()

    companion object {
        private const val EXTRA_EMAIL = "extra_email"
        private const val EXTRA_PASSWORD = "extra_password"

        fun getIntent(context: Context, email: String, password: String): Intent {
            return Intent(context, TallyActivity::class.java).apply {
                putExtra(EXTRA_EMAIL, email)
                putExtra(EXTRA_PASSWORD, password)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tally)

        val customProgressDialog = CustomProgressDialog(this)

        val email = intent.getStringExtra(EXTRA_EMAIL) ?: ""
        val password = intent.getStringExtra(EXTRA_PASSWORD) ?: ""
        authenticateBank(email, password, customProgressDialog)

        val tabPager = findViewById<ViewPager>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tally_tab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        initTabs(tabLayout, tabPager)
    }

    private fun initTabs(tabLayout: TabLayout, tabPager: ViewPager) {
        val tabFragments = ArrayList<Fragment>()
        tabFragments.add(CardTokenizationFragment())
        tabFragments.add(TokenizedCardsTransactionFragment())
        tabFragments.add(TallyMerchantsFragment())

        val tabTitles = ArrayList<String>()
        tabTitles.add("CARDS")
        tabTitles.add("TRANSACTION")
        tabTitles.add("MERCHANTS")

        tabPager.adapter = TabPagerAdapter(supportFragmentManager, tabFragments, tabTitles)
        tabPager.setPadding(0, 0, 0, 0)
        tabPager.pageMargin = 0
        tabLayout.setupWithViewPager(tabPager)
    }

    private fun authenticateBank(
        email: String,
        password: String,
        customProgressDialog: CustomProgressDialog
    ) {
        customProgressDialog.show()
        customProgressDialog.setUpdateText("Authentication...")

        tallyQrcodeGenerator.authenticateBank(
            email = email,
            password = password,
            object : TallyResponseCallback<LoginResponse> {
                override fun success(data: LoginResponse?) {
                    customProgressDialog.setUpdateText("Authentication Successful")
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            customProgressDialog.dismiss()
                        }, 2000
                    )
                }

                override fun failed(message: String?) {
                    customProgressDialog.setUpdateText(message.toString())
                    customProgressDialog.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed({ onBackPressed() }, 2000)
                }
            }
        )
    }
}