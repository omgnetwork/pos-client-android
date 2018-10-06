package network.omisego.omgwallet.base

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.IdlingRegistry
import androidx.test.rule.ActivityTestRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.R
import network.omisego.omgwallet.config.LocalClientSetup
import network.omisego.omgwallet.network.ClientProvider
import org.junit.Rule

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
open class BaseInstrumentalTest {
    private val idlingResource by lazy {
        OkHttp3IdlingResource.create("OkHTTP", ClientProvider.eWalletClient.client)
    }

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    val sharedPreferences: SharedPreferences by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    val toolbarTitle: String
        get() = rule.activity.supportActionBar?.title!!.toString()

    fun clearSharePreference() {
        sharedPreferences.edit().clear().apply()
    }

    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    fun stringRes(@StringRes id: Int): String {
        return rule.activity.getString(id)
    }

    fun setupClientProvider() {
        ClientProvider.init(LocalClientSetup())
    }

    fun start() {
        rule.launchActivity(Intent())
    }
}