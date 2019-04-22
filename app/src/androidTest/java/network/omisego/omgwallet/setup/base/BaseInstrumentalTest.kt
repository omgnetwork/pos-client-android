package network.omisego.omgwallet.setup.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Intent
import androidx.annotation.StringRes
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import co.infinum.goldfinger.Goldfinger
import co.omisego.omisego.OMGAPIClient
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.WalletList
import co.omisego.omisego.network.ewallet.EWalletClient
import com.jakewharton.espresso.OkHttp3IdlingResource.create
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.repository.LocalRepository
import network.omisego.omgwallet.setup.config.TxConsumerClient
import network.omisego.omgwallet.setup.screen.MainScreen
import network.omisego.omgwallet.setup.util.NetworkMockUtil
import network.omisego.omgwallet.storage.SessionStorage
import network.omisego.omgwallet.storage.Storage
import network.omisego.omgwallet.storage.Storage.Companion.create
import network.omisego.omgwallet.util.ContextUtil
import network.omisego.omgwallet.util.IdlingResourceUtil
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import java.math.BigDecimal

open class BaseInstrumentalTest {
    private val idlingResource by lazy { create("OkHTTP", eWalletClient.client) }
    private val consumerIdlingResource by lazy { create("OkHTTPConsumer", consumerClient.okHttpClient) }
    private val mainScreen: MainScreen by lazy { MainScreen() }
    private lateinit var eWalletClient: EWalletClient
    private val storage: Storage by lazy { create(getInstrumentation().targetContext) }
    private val sessionStorage: SessionStorage by lazy { SessionStorage(storage) }
    val context by lazy { getInstrumentation().targetContext }
    val localRepository: LocalRepository by lazy { LocalRepository(storage, sessionStorage) }
    val consumerClient by lazy { TxConsumerClient.create() }
    lateinit var client: OMGAPIClient
    lateinit var mockWebServer: MockWebServer
    lateinit var testUrl: HttpUrl

    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    fun hasToolbarTitle(title: String) {
        mainScreen.toolbar.hasTitle(title)
    }

    fun clearSharePreference() {
        localRepository.deleteAll()
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

    fun setupMockWebServer() {
        ClientProvider.setTestEWalletClient(null)
        mockWebServer = NetworkMockUtil.createMockWebServer()
    }

    fun setupTestUrl() {
        testUrl = NetworkMockUtil.createMockUrl(mockWebServer)
        ClientProvider.setTestUrl(testUrl)
    }

    fun setupClient() {
        eWalletClient = ClientProvider.createClient()
        client = OMGAPIClient(eWalletClient)
        ClientProvider.setTestEWalletClient(eWalletClient)
    }

    fun start() {
        rule.launchActivity(Intent())
    }
}