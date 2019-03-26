package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.espresso.action.GeneralLocation
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.omisego.omisego.model.params.LoginParams
import com.agoda.kakao.screen.Screen.Companion.idle
import network.omisego.omgwallet.setup.base.BaseInstrumentalTest
import network.omisego.omgwallet.setup.config.TestData
import network.omisego.omgwallet.setup.screen.ConfirmFingerprintScreen
import network.omisego.omgwallet.setup.screen.LoginScreen
import network.omisego.omgwallet.setup.screen.MainScreen
import network.omisego.omgwallet.setup.screen.ProfileScreen
import org.amshove.kluent.shouldBe
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileTest : BaseInstrumentalTest() {
    private val profileScreen: ProfileScreen by lazy { ProfileScreen() }
    private val mainScreen: MainScreen by lazy { MainScreen() }
    private val loginScreen: LoginScreen by lazy { LoginScreen() }
    private val confirmFingerprintScreen: ConfirmFingerprintScreen by lazy { ConfirmFingerprintScreen() }

    companion object : BaseInstrumentalTest() {

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
        localRepository.deleteFingerprintSession()
        localRepository.deleteTransactionRequest()
        registerIdlingResource()
        start()
        mainScreen.bottomNavigation.setSelectedItem(R.id.profile)
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testSignOut() {
        profileScreen {
            scrollView.scrollToEnd()
            idle(300)
            tvSignOut.click(GeneralLocation.CENTER_LEFT)
        }

        loginScreen {
            tilEmail.isDisplayed()
            tilPassword.isDisplayed()
            btnLogin.isDisplayed()
            btnFingerprint.isDisplayed()
            tvSignUp.isDisplayed()
        }

        with(localRepository) {
            hasUser() shouldBe false
            hasWallet() shouldBe false
            hasAuthenticationToken() shouldBe false
        }
    }

    @Test
    fun testVersionAndServerIsDisplayed() {
        profileScreen {
            tvEndpointName.startsWithText("http")
            tvVersionName.hasText(BuildConfig.VERSION_NAME)
        }
    }

    @Test
    fun testFingerprintConfirmDialogIsDisplay() {
        if (hasFingerprint()) {
            profileScreen {
                switch {
                    click()
                }
            }
            confirmFingerprintScreen {
                tvTitle.isDisplayed()
                btnConfirm.isDisplayed()
                tilPassword.isDisplayed()
            }
        }
    }

    @Test
    fun testCancelFingerprintConfirmDialogShouldDismiss() {
        if (hasFingerprint()) {
            profileScreen {
                switch {
                    isNotChecked()
                    click()
                }
            }
            confirmFingerprintScreen {
                closeSoftKeyboard()
                pressBack()
            }
            profileScreen {
                switch.isNotChecked()
                tvFingerprintTitle.isDisplayed()
                tvSignOut.isDisplayed()
                tvTransaction.isDisplayed()
            }
        }
    }

    @Test
    fun testEnableFingerprints() {
        if (hasFingerprint()) {
            registerIdlingResource()
            profileScreen {
                switch {
                    isNotChecked()
                    click()
                }
            }
            confirmFingerprintScreen {
                tilPassword.edit {
                    typeText(TestData.USER_PASSWORD)
                }
                closeSoftKeyboard()
                btnConfirm.click()
            }
            profileScreen {
                tvFingerprintTitle.isDisplayed()
                tvSignOut.isDisplayed()
                tvTransaction.isDisplayed()
                switch.isChecked()
            }
            unregisterIdlingResource()
        }
    }
}