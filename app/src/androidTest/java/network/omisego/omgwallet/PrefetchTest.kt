package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.espresso.IdlingRegistry
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgwallet.base.BaseInstrumentalTest
import network.omisego.omgwallet.config.LocalClientSetup
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.screen.SplashScreen
import network.omisego.omgwallet.storage.Storage
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldEqualTo
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class PrefetchTest : BaseInstrumentalTest() {
    private val splashScreen: SplashScreen by lazy { SplashScreen() }
    private val mainScreen: MainScreen by lazy { MainScreen() }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            Storage.clearSession()
            ClientProvider.init(LocalClientSetup())
            val response = ClientProvider.client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
            Storage.saveUser(response.body()!!.data.user)
            Storage.saveCredential(Credential(response.body()!!.data.authenticationToken))
            Storage.saveUserEmail(TestData.USER_EMAIL)
        }
    }

    @Before
    fun setup() {
        start()
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testSplashScreenShouldShowInformationCorrectly() {
        splashScreen {
            loadingGif.isDisplayed()
            tvStatus.hasText(R.string.splash_status_loading_wallet)
            IdlingRegistry.getInstance().register(idlingResource)
            tvStatus.hasText(R.string.splash_status_creating_transaction)
            IdlingRegistry.getInstance().register(coroutineIdlingResource)
        }
    }

    @Test
    fun testSplashScreenShouldLoadWalletAndSaveCorrectly() {
        registerIdlingResource()

        Storage.loadWallets()?.data?.size?.shouldBeGreaterThan(0)
    }

    @Test
    fun testSplashScreenShouldCreateTransactionRequestAndSaveIdCorrectly() {
        registerIdlingResource()

        /* Should create a transaction request and save the id with format `id1|id2` */
        Storage.loadFormattedId().split("|").size shouldEqualTo 2
    }
}
