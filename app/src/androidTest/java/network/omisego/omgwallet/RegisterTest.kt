package network.omisego.omgwallet

import androidx.navigation.fragment.NavHostFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import network.omisego.omgwallet.R
import network.omisego.omgwallet.setup.base.BaseInstrumentalTest
import network.omisego.omgwallet.setup.config.TestData
import network.omisego.omgwallet.setup.extensions.clickThenReplace
import network.omisego.omgwallet.setup.screen.ConfirmScreen
import network.omisego.omgwallet.setup.screen.RegisterScreen
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
@RunWith(AndroidJUnit4::class)

class RegisterTest : BaseInstrumentalTest() {
    val registerScreen: RegisterScreen by lazy { RegisterScreen() }
    val confirmScreen: ConfirmScreen by lazy { ConfirmScreen() }

    @Before
    fun setup() {
        clearSharePreference()
        setupClient()
        registerIdlingResource()
        start()
        NavHostFragment.findNavController(
            rule.activity.supportFragmentManager.fragments[0]
        ).navigate(R.id.action_signInFragment_to_signupFragment)
    }

    @After
    fun teardown() {
        unregisterIdlingResource()
    }

    @Test
    fun testShowAllFormElement() {
        registerScreen {
            tilEmail.isDisplayed()
            tilFullname.isDisplayed()
            tilPassword.isDisplayed()
            tilRetypePassword.isDisplayed()
        }
    }

    @Test
    fun testShowErrorText() {
        unregisterIdlingResource()
        registerScreen {
            tilEmail.edit.typeText("test")
            closeSoftKeyboard()
            tilPassword.edit.typeText("1234")
            closeSoftKeyboard()
            scrollView.scrollToEnd()
            btnSignUp.click()
            tilFullname.hasError(stringRes(R.string.validator_signup_fullname_not_empty))
            tilEmail.hasError(stringRes(R.string.validator_signin_email_invalid_format))
            tilRetypePassword.hasError(stringRes(R.string.validator_signup_confirm_password_not_matched))
            tilPassword.hasError(stringRes(R.string.validator_signup_password_at_least_8))
        }
    }

    @Test
    fun testShowErrorTextPasswordContainLowerCase() {
        unregisterIdlingResource()
        registerScreen {
            tilPassword.edit { typeText("TEST1234") }
            closeSoftKeyboard()
            scrollView.scrollToEnd()
            btnSignUp.click()
            tilPassword {
                hasError(stringRes(R.string.validator_signup_password_lower_case))
            }
        }
    }

    @Test
    fun testShowErrorTextPasswordContainUpperCase() {
        unregisterIdlingResource()
        registerScreen {
            tilPassword.edit { typeText("test1234") }
            closeSoftKeyboard()
            btnSignUp.click()
            tilPassword {
                hasError(stringRes(R.string.validator_signup_password_upper_case))
            }
        }
    }

    @Test
    fun testShowErrorTextPasswordContainNumeric() {
        unregisterIdlingResource()
        registerScreen {
            tilPassword.edit { typeText("Test####") }
            closeSoftKeyboard()
            scrollView.scrollToEnd()
            btnSignUp.click()
            tilPassword {
                hasError(stringRes(R.string.validator_signup_password_numeric))
            }
        }
    }

    @Test
    fun testShowErrorTextPasswordContainSpecialChar() {
        unregisterIdlingResource()
        registerScreen {
            tilPassword.edit { typeText("Test1234") }
            closeSoftKeyboard()
            scrollView.scrollToEnd()
            btnSignUp.click()
            tilPassword {
                hasError(stringRes(R.string.validator_signup_password_special_char))
            }
        }
    }

    @Test
    fun testNotShowAnyError() {
        unregisterIdlingResource()
        registerScreen {
            tilFullname.edit.clickThenReplace("a")
            closeSoftKeyboard()
            tilEmail.edit.clickThenReplace(TestData.REGISTER_USER_EMAIL)
            closeSoftKeyboard()
            tilPassword.edit.clickThenReplace("Tt123###")
            closeSoftKeyboard()
            tilRetypePassword.edit.clickThenReplace("Tt123###")
            closeSoftKeyboard()
            scrollView.scrollToEnd()
            btnSignUp.click()
            tilRetypePassword.isErrorDisabled()
            tilPassword.isErrorDisabled()
            scrollView.scrollToStart()
            tilFullname.isErrorDisabled()
            tilEmail.isErrorDisabled()
        }
    }

    @Test
    fun testRegisterSuccessfully() {
        unregisterIdlingResource()
        registerScreen {
            idle(300)
            tilFullname.edit.clickThenReplace("a")
            closeSoftKeyboard()
            tilEmail.edit.clickThenReplace(TestData.REGISTER_USER_EMAIL)
            closeSoftKeyboard()
            tilPassword.edit.clickThenReplace("Tt123###")
            closeSoftKeyboard()
            tilRetypePassword.edit.clickThenReplace("Tt123###")
            closeSoftKeyboard()
            scrollView.scrollToEnd()
            btnSignUp.click()
            registerIdlingResource()
        }
        confirmScreen {
            tvTitle.isDisplayed()
            tvDescription.isDisplayed()
            btnGotIt.isDisplayed()
            ivLogo.isDisplayed()
        }
    }
}