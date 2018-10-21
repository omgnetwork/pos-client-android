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
import network.omisego.omgwallet.config.MockData
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.screen.QRScreen
import network.omisego.omgwallet.storage.Storage
import org.amshove.kluent.shouldEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QRTest : BaseInstrumentalTest() {
    private val qrScreen: QRScreen by lazy { QRScreen() }
    private val mainScreen: MainScreen by lazy { MainScreen() }

    @Before
    fun setup() {
        ClientProvider.init(LocalClientSetup())
        Storage.clearSession()
        val response = ClientProvider.client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
        Storage.saveWallets(MockData.walletList)
        Storage.saveUser(response.body()!!.data.user)
        Storage.saveCredential(Credential(response.body()!!.data.authenticationToken))
        Storage.saveUserEmail(TestData.USER_EMAIL)
        Storage.deleteFingerprintCredential()
        Storage.saveFingerprintOption(false)
        start()
        registerIdlingResource()
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testShowQR() {
        mainScreen.fabQR.click()
        qrScreen {
            toolbarTitle shouldEqualTo stringRes(R.string.show_qr_title)
            ivQR {
                isDisplayed()
            }
        }
    }

    @Test
    fun testCloseQR() {
        mainScreen.fabQR.click()
        toolbarTitle shouldEqualTo stringRes(R.string.show_qr_title)
        qrScreen.pressBack()
        toolbarTitle shouldEqualTo stringRes(R.string.balance_title)
        mainScreen.bottomNavigation.setSelectedItem(R.id.profile)
        mainScreen.fabQR.click()
        qrScreen.pressBack()
        toolbarTitle shouldEqualTo stringRes(R.string.profile_title)
    }
}