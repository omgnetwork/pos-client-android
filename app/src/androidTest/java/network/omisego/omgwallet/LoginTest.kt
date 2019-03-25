package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.ext.junit.runners.AndroidJUnit4
import network.omisego.omgwallet.setup.base.BaseInstrumentalTest
import network.omisego.omgwallet.setup.config.MockData
import network.omisego.omgwallet.setup.config.TestData
import network.omisego.omgwallet.setup.screen.LoginScreen
import network.omisego.omgwallet.setup.screen.MainScreen
import org.amshove.kluent.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest : BaseInstrumentalTest() {
    private val loginScreen: LoginScreen by lazy { LoginScreen() }
    private val mainScreen: MainScreen by lazy { MainScreen() }

    @Before
    fun setup() {
        clearSharePreference()
        setupClient()
        registerIdlingResource()
        start()
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testButtonDisabledAfterSignIn() {
        unregisterIdlingResource()
        loginScreen {
            tilEmail.edit.typeText("a@b.com")
            tilPassword.edit.typeText("12345678")
            btnLogin.click()
            btnLogin.isDisabled()
            btnFingerprint.isDisabled()
        }
    }

    @Test
    fun testErrorTextDisplayAfterClickLogin() {
        unregisterIdlingResource()
        loginScreen {
            tilEmail.edit.typeText("hello")
            tilPassword.edit.typeText("pass")
            btnLogin.click()
            tilEmail.hasError(stringRes(R.string.validator_signin_email_invalid_format))
            tilPassword.hasError(stringRes(R.string.validator_signup_password_at_least_8))
        }
    }

    @Test
    fun testLogin() {
        loginScreen {
            tilEmail.edit.typeText(TestData.USER_EMAIL)
            tilPassword.edit.typeText(TestData.USER_PASSWORD)
            localRepository.saveWallets(MockData.walletList)
            btnLogin.click()
            mainScreen {
                fabQR.isDisplayed()
            }
            with(localRepository) {
                hasAuthenticationToken() shouldBe true
                hasUser() shouldBe true
            }
        }
    }

    @Test
    fun testLoginPageAppearance() {
        loginScreen {
            tilEmail.isDisplayed()
            tilPassword.isDisplayed()
            btnLogin.isDisplayed()
            btnFingerprint.isDisplayed()
            tvSignUp.isDisplayed()
            tvVersion.isDisplayed()
        }
    }
}
