package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.ext.junit.runners.AndroidJUnit4
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgwallet.setup.base.BaseInstrumentalTest
import network.omisego.omgwallet.setup.config.MockData
import network.omisego.omgwallet.setup.config.TestData
import network.omisego.omgwallet.setup.custom.assertions.onToast
import network.omisego.omgwallet.setup.custom.matchers.ToastMatcher
import network.omisego.omgwallet.setup.screen.MainScreen
import network.omisego.omgwallet.setup.screen.ProfileScreen
import network.omisego.omgwallet.setup.screen.TransactionListScreen
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
        localRepository.deleteSession()
        localRepository.deleteFingerprintSession()
        localRepository.saveWallets(MockData.walletList)
        val response = client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
        val clientAuthenticationToken = response.body()?.data!!
        localRepository.saveSession(clientAuthenticationToken)
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
    fun testShowTransactionToastWhenClickingOnTheRecord() {
        mainScreen {
            bottomNavigation.setSelectedItem(R.id.profile)
        }
        profileScreen {
            tvTransaction.click()
        }
        transactionListScreen {
            recyclerView {
                isDisplayed()
                firstChild<TransactionListScreen.Item> {
                    click()
                    onToast(ToastMatcher.notContainsNull())
                }
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