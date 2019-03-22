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
import network.omisego.omgwallet.config.MockData
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.screen.ProfileScreen
import network.omisego.omgwallet.screen.TransactionListScreen
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionListTest : BaseInstrumentalTest() {
    private val mainScreen: MainScreen by lazy { MainScreen() }
    private val profileScreen: ProfileScreen by lazy { ProfileScreen() }
    private val transactionListScreen: TransactionListScreen by lazy { TransactionListScreen() }

    @Before
    fun setup() {
        setupClient()
        registerIdlingResource()
        sessionStorage.clear()
        val response = client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
        val clientAuthenticationToken = response.body()?.data!!
        sessionStorage.save(clientAuthenticationToken)
        storage.saveWallets(MockData.walletList)
        storage.deleteFingerprintCredential()
        storage.saveFingerprintOption(false)
        start()
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testShowTransactionList() {
        mainScreen {
            bottomNavigation.setSelectedItem(R.id.profile)
        }
        profileScreen {
            tvTransaction.click()
        }
        transactionListScreen {
            recyclerView {
                isDisplayed()
            }
        }
    }

    @Test
    fun testCloseTransactionList() {
        mainScreen.bottomNavigation.setSelectedItem(R.id.profile)
        profileScreen.tvTransaction.click()
        mainScreen.pressBack()
        profileScreen {
            tvTransaction.isDisplayed()
            tvFingerprintTitle.isDisplayed()
            tvSignOut.isDisplayed()
        }
    }
}