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
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.screen.ProfileScreen
import network.omisego.omgwallet.screen.TransactionListScreen
import network.omisego.omgwallet.storage.Storage
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
        setupClientProvider()
        registerIdlingResource()
        Storage.clearSession()
        val response = ClientProvider.client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
        Storage.saveWallets(MockData.walletList)
        Storage.saveUser(response.body()!!.data.user)
        Storage.saveCredential(Credential(response.body()!!.data.authenticationToken))
        Storage.saveUserEmail(TestData.USER_EMAIL)
        Storage.deleteFingerprintCredential()
        Storage.saveFingerprintOption(false)
        start()
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testShowTransactionList() {
        mainScreen {
            bottomBarProfile.click()
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
        mainScreen {
            bottomBarProfile.click()
        }
        profileScreen {
            tvTransaction.click()
        }
        transactionListScreen {
            backButton.click()
        }
        profileScreen {
            tvTransaction.isDisplayed()
            tvFingerprintTitle.isDisplayed()
            tvSignOut.isDisplayed()
        }
    }
}