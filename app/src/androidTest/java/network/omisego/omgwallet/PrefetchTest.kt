package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgwallet.base.BaseInstrumentalTest
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.screen.SplashScreen
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

    companion object: BaseInstrumentalTest() {

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            setupClient()
            localRepository.deleteSession()
            val response = client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
            val clientAuthenticationToken = response.body()?.data!!
            localRepository.saveSession(clientAuthenticationToken)
        }
    }

    @Before
    fun setup() {
        setupClient()
        localRepository.deleteWallets()
        localRepository.deleteTransactionRequest()
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
        localRepository.loadWallets()?.data?.size?.shouldBeGreaterThan(0)
    }

    @Test
    fun testSplashScreenShouldCreateTransactionRequestAndSaveIdCorrectly() {
        registerIdlingResource()
        start()

        /* Should create a transaction request and save the id with format `id1|id2` */
        mainScreen.bottomNavigation.isDisplayed()
        localRepository.loadTransactionRequest().split("|").size shouldEqualTo 2
    }

    @Test
    fun testSelectPrimaryTokenAutomaticallyWhenTheTokenPrimaryIdIsNull() {
        /* Delete token primary first so that the primary token is null */
        localRepository.deleteTokenPrimary()

        /* Verify that the token has already deleted */
        localRepository.loadTokenPrimary() shouldBe null

        registerIdlingResource()
        start()

        mainScreen.bottomNavigation.isDisplayed()
        selectPrimaryToken(localRepository.loadWallets()!!, null).id shouldEqual localRepository.loadTokenPrimary()
    }
}
