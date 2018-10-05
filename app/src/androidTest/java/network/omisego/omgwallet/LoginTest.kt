package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import network.omisego.omgwallet.base.BaseInstrumentalTest
import network.omisego.omgwallet.screen.BalanceScreen
import network.omisego.omgwallet.screen.LoginScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest: BaseInstrumentalTest() {
    @Rule
    @JvmField
    val rule = ActivityTestRule(MainActivity::class.java, true, false)

    private val loginScreen: LoginScreen by lazy { LoginScreen() }
    private val balanceScreen: BalanceScreen by lazy { BalanceScreen() }

    @Before
    fun setup() {
        clearSharePreference()
        rule.launchActivity(Intent())
    }

    @Test
    fun testLoginPageShouldAppearOnTheScreenProperly() {
        loginScreen {
            tilEmail.isDisplayed()
            tilPassword.isDisplayed()
            btnLogin.isDisplayed()
            btnFingerprint.isDisplayed()
            tvSignUp.isDisplayed()
        }
    }

    @Test
    fun errorTextShouldBeDisplayAfterClickLogin() {
        loginScreen {
            tilEmail.edit.typeText("hello")
            tilPassword.edit.typeText("pass")
            btnLogin.click()
            tilEmail.hasError("Email Address is invalid format")
            tilPassword.hasError("Password must contain at least 8 characters")
        }
    }

    @Test
    fun buttonShouldBeDisabledAfterSignIn() {
        loginScreen {
            tilEmail.edit.typeText("a@b.com")
            tilPassword.edit.typeText("12345678")
            btnLogin.click()
            btnLogin.isDisabled()
            btnFingerprint.isDisabled()
        }
    }

    @Test
    fun shouldLoginSuccessfully() {
        loginScreen {
            tilEmail.edit.typeText("phuchit@omise.co")
            tilPassword.edit.typeText("test1234")
            btnLogin.click()
            Thread.sleep(3000)
            balanceScreen {
                recyclerView {
                    isDisplayed()
                    hasSize(2)
                    firstChild<BalanceScreen.Item> {
                        isVisible()
                        tvTokenLogo {
                            isVisible()
                            hasText("CGO")
                        }
                        tvCurrencyName {
                            hasText("Coffee GO")
                        }
                    }
                }
            }
        }
    }
}
