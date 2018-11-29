package network.omisego.omgwallet.base

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.IdlingRegistry
import androidx.test.rule.ActivityTestRule
import co.infinum.goldfinger.Goldfinger
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.WalletList
import com.jakewharton.espresso.OkHttp3IdlingResource
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.R
import network.omisego.omgwallet.config.LocalClientSetup
import network.omisego.omgwallet.config.TxConsumerClient
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.util.ContextUtil
import network.omisego.omgwallet.util.IdlingResourceUtil
import org.junit.Rule
import java.math.BigDecimal

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
open class BaseInstrumentalTest {
    val consumerClient by lazy {
        TxConsumerClient.create()
    }

    val idlingResource by lazy {
        OkHttp3IdlingResource.create("OkHTTP", ClientProvider.eWalletClient.client)
    }

    val consumerIdlingResource by lazy {
        OkHttp3IdlingResource.create("OkHTTPConsumer", consumerClient.okHttpClient)
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
        IdlingRegistry.getInstance().register(consumerIdlingResource)
        IdlingRegistry.getInstance().register(IdlingResourceUtil.idlingResource)
    }

    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        IdlingRegistry.getInstance().unregister(consumerIdlingResource)
        IdlingRegistry.getInstance().unregister(IdlingResourceUtil.idlingResource)
    }

    fun selectPrimaryToken(walletList: WalletList, primaryTokenId: String?): Token {
        val theHighestBalance: (Balance) -> BigDecimal = { it.amount.divide(it.token.subunitToUnit) }
        return walletList.data[0].balances.find { it.token.id == primaryTokenId }?.token
            ?: walletList.data[0].balances.maxBy(theHighestBalance)?.token
            ?: walletList.data[0].balances[0].token
    }

    fun stringRes(@StringRes id: Int): String {
        return rule.activity.getString(id)
    }

    fun setupClientProvider() {
        ClientProvider.initHTTPClient(LocalClientSetup())
    }

    fun start() {
        rule.launchActivity(Intent())
    }
}