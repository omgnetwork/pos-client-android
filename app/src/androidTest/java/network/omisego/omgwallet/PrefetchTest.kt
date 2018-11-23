package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgwallet.base.BaseInstrumentalTest
import network.omisego.omgwallet.config.LocalClientSetup
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.screen.SplashScreen
import network.omisego.omgwallet.storage.Storage
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldEqual
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
            ClientProvider.initHTTPClient(LocalClientSetup())
            val response = ClientProvider.client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
            Storage.saveUser(response.body()!!.data.user)
            Storage.saveCredential(Credential(response.body()!!.data.authenticationToken))
            Storage.saveUserEmail(TestData.USER_EMAIL)
        }
    }

    @Before
    fun setup() {
        Storage.deleteWallets()
        Storage.deleteFormattedIds()
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testSplashScreenShouldDisplayCorrectly() {
        start()
        splashScreen {
            loadingGif.isVisible()
            tvStatus.isDisplayed()
            btnClose.isNotDisplayed()
        }
    }

    @Test
    fun testSplashScreenShouldLoadWalletAndSaveCorrectly() {
        registerIdlingResource()
        start()

        /* This will ensure that the test framework will be waiting for the idle state */
        mainScreen.bottomNavigation.isDisplayed()
        Storage.loadWallets()?.data?.size?.shouldBeGreaterThan(0)
    }

    @Test
    fun testSplashScreenShouldCreateTransactionRequestAndSaveIdCorrectly() {
        registerIdlingResource()
        start()

        /* Should create a transaction request and save the id with format `id1|id2` */
        mainScreen.bottomNavigation.isDisplayed()
        Storage.loadFormattedId().split("|").size shouldEqualTo 2
    }

    @Test
    fun testSelectPrimaryTokenAutomaticallyWhenTheTokenPrimaryIdIsNull() {
        /* Delete token primary first so that the primary token is null */
        Storage.deleteTokenPrimary()

        /* Verify that the token has already deleted */
        Storage.loadTokenPrimary() shouldBe null

        registerIdlingResource()
        start()

        mainScreen.bottomNavigation.isDisplayed()
        selectPrimaryToken(Storage.loadWallets()!!, null).id shouldEqual Storage.loadTokenPrimary()
    }
}
