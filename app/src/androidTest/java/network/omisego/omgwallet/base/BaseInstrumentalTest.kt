package network.omisego.omgwallet.base

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.rule.ActivityTestRule
import co.infinum.goldfinger.Goldfinger
import com.jakewharton.espresso.OkHttp3IdlingResource
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.R
import network.omisego.omgwallet.config.LocalClientSetup
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.util.ContextUtil
import network.omisego.omgwallet.util.IdlingResourceUtil
import org.junit.Rule

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
open class BaseInstrumentalTest {
    val idlingResource by lazy {
        OkHttp3IdlingResource.create("OkHTTP", ClientProvider.eWalletClient.client)
    }
    val coroutineIdlingResource by lazy {
        CountingIdlingResource("coroutines")
    }

    private val mainScreen: MainScreen by lazy { MainScreen() }

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    val sharedPreferences: SharedPreferences by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    fun hasToolbarTitle(title: String) {
        mainScreen.toolbar.hasTitle(title)
    }

    fun clearSharePreference() {
        sharedPreferences.edit().clear().apply()
    }

    fun hasFingerprint() = Goldfinger.Builder(ContextUtil.context).build().hasFingerprintHardware()

    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(idlingResource)
        IdlingRegistry.getInstance().register(coroutineIdlingResource)
        IdlingResourceUtil.idlingResource = coroutineIdlingResource
    }

    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        IdlingRegistry.getInstance().unregister(coroutineIdlingResource)
        IdlingResourceUtil.idlingResource = null
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