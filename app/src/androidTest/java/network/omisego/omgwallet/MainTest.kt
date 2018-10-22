package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.runner.AndroidJUnit4
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgwallet.base.BaseInstrumentalTest
import network.omisego.omgwallet.config.LocalClientSetup
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.screen.BalanceScreen
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.screen.ProfileScreen
import network.omisego.omgwallet.screen.SplashScreen
import network.omisego.omgwallet.storage.Storage
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainTest : BaseInstrumentalTest() {
    private val balanceScreen: BalanceScreen by lazy { BalanceScreen() }
    private val splashScreen: SplashScreen by lazy { SplashScreen() }
    private val mainScreen: MainScreen by lazy { MainScreen() }
    private val profileScreen: ProfileScreen by lazy { ProfileScreen() }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            ClientProvider.init(LocalClientSetup())
            Storage.clearSession()
            val response = ClientProvider.client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
            Storage.saveUser(response.body()!!.data.user)
            Storage.saveCredential(Credential(response.body()!!.data.authenticationToken))
            Storage.saveUserEmail(TestData.USER_EMAIL)
        }
    }

    @Before
    fun setup() {
        registerIdlingResource()
        start()
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testShowMainBottomBar() {
        mainScreen {
            fabQR.isDisplayed()
            bottomNavigation.isDisplayed()
        }
    }

    @Test
    fun testShowBalance() {
        balanceScreen {
            recyclerView {
                isDisplayed()
                firstChild<BalanceScreen.Item> {
                    isVisible()
                    tvTokenLogo {
                        isVisible()
                    }
                    tvCurrencyName {
                        isVisible()
                    }
                }
            }
        }
        hasToolbarTitle(stringRes(R.string.balance_title))
    }

    @Test
    fun testShowProfile() {
        mainScreen.bottomNavigation.setSelectedItem(R.id.profile)
        hasToolbarTitle(stringRes(R.string.profile_title))
        profileScreen {
            tvTransaction.isDisplayed()
            tvFingerprintTitle.isDisplayed()
            tvSignOut.isDisplayed()
        }
    }

    @Test
    fun testRepeatlySwitchTab() {
        for (i in 0..1) {
            mainScreen.bottomNavigation.setSelectedItem(R.id.profile)
            hasToolbarTitle(stringRes(R.string.profile_title))
            profileScreen.tvTransaction.isDisplayed()
            mainScreen.bottomNavigation.setSelectedItem(R.id.balance)
            hasToolbarTitle(stringRes(R.string.balance_title))
            balanceScreen.recyclerView.isDisplayed()
        }
    }
}
