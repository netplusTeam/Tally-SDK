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
import com.netplus.coremechanism.utils.TallyCustomProgressDialog
import com.netplus.coremechanism.utils.TallyQrcodeGenerator
import com.netplus.coremechanism.utils.TallyResponseCallback
import com.netplus.tallyqrgeneratorui.adapters.TabPagerAdapter
import com.netplus.tallyqrgeneratorui.fragments.AllTokenizedCardsFragment
import com.netplus.tallyqrgeneratorui.fragments.CardTokenizationFragment
import com.netplus.tallyqrgeneratorui.fragments.TallyMerchantsFragment
import com.netplus.tallyqrgeneratorui.fragments.TokenizedCardsTransactionFragment

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

        val tallyCustomProgressDialog = TallyCustomProgressDialog(this)

        val email = intent.getStringExtra(EXTRA_EMAIL) ?: ""
        val password = intent.getStringExtra(EXTRA_PASSWORD) ?: ""
        authenticateBank(email, password, tallyCustomProgressDialog)

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

        val tabFragments = ArrayList<Fragment>().apply {
            add(CardTokenizationFragment())
            add(AllTokenizedCardsFragment())
            add(TokenizedCardsTransactionFragment())
            add(TallyMerchantsFragment())
        }

        val tabTitles = ArrayList<String>().apply {
            add("Cards")
            add("Tokenized Cards")
            add("All Transactions")
            add("All Merchants")
        }

        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabPager.adapter = TabPagerAdapter(supportFragmentManager, tabFragments, tabTitles)
        tabPager.setPadding(0, 0, 0, 0)
        tabPager.pageMargin = 0
        tabLayout.setupWithViewPager(tabPager)
    }

    private fun authenticateBank(
        email: String,
        password: String,
        tallyCustomProgressDialog: TallyCustomProgressDialog
    ) {
        tallyCustomProgressDialog.show()
        tallyCustomProgressDialog.setUpdateText("Authentication...")

        tallyQrcodeGenerator.authenticateBank(
            email = email,
            password = password,
            object : TallyResponseCallback<LoginResponse> {
                override fun success(data: LoginResponse?) {
                    tallyCustomProgressDialog.setUpdateText("Authentication Successful")
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            tallyCustomProgressDialog.dismiss()
                        }, 2000
                    )
                }

                override fun failed(message: String?) {
                    tallyCustomProgressDialog.setUpdateText(message.toString())
                    tallyCustomProgressDialog.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed({ onBackPressed() }, 2000)
                }
            }
        )
    }
}