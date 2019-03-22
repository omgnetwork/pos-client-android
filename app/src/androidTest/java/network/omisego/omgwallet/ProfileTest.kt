package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.espresso.action.GeneralLocation
import androidx.test.runner.AndroidJUnit4
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgwallet.base.BaseInstrumentalTest
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.screen.ConfirmFingerprintScreen
import network.omisego.omgwallet.screen.LoginScreen
import network.omisego.omgwallet.screen.MainScreen
import network.omisego.omgwallet.screen.ProfileScreen
import network.omisego.omgwallet.storage.StorageKey
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
            sessionStorage.clear()
            val response = client.login(LoginParams(TestData.USER_EMAIL, TestData.USER_PASSWORD)).execute()
            val clientAuthenticationToken = response.body()?.data!!
            sessionStorage.save(clientAuthenticationToken)
        }
    }

    @Before
    fun setup() {
        setupClient()
        storage.deleteFingerprintCredential()
        storage.saveFingerprintOption(false)
        storage.deleteFormattedIds()
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

        with(storage) {
            has(StorageKey.KEY_USER) shouldBe false
            has(StorageKey.KEY_WALLET) shouldBe false
            has(StorageKey.KEY_AUTHENTICATION_TOKEN) shouldBe false
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