package network.omisego.omgwallet

import androidx.navigation.fragment.NavHostFragment
import androidx.test.runner.AndroidJUnit4
import network.omisego.omgwallet.base.BaseInstrumentalTest
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.screen.ConfirmScreen
import network.omisego.omgwallet.screen.RegisterScreen
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
        setupClientProvider()
        registerIdlingResource()
        clearSharePreference()
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
            tilPassword.edit.typeText("1234")
            btnSignUp.click()
            tilFullname.hasError("Field should not be empty")
            tilEmail.hasError("Email Address is invalid format")
            tilRetypePassword.hasError("Password is not matched")
            tilPassword.hasError("Password must contain at least 8 characters")
        }
    }

    @Test
    fun testShowErrorTextPasswordContainLowerCase() {
        unregisterIdlingResource()
        registerScreen {
            tilPassword.edit { typeText("TEST1234") }
            btnSignUp.click()
            tilPassword {
                hasError("Password must contain at least 1 lower case character")
            }
        }
    }

    @Test
    fun testShowErrorTextPasswordContainUpperCase() {
        unregisterIdlingResource()
        registerScreen {
            tilPassword.edit { typeText("test1234") }
            btnSignUp.click()
            tilPassword {
                hasError("Password must contain at least 1 upper case character")
            }
        }
    }

    @Test
    fun testShowErrorTextPasswordContainNumeric() {
        unregisterIdlingResource()
        registerScreen {
            tilPassword.edit { typeText("Test####") }
            btnSignUp.click()
            tilPassword {
                hasError("Password must contain at least 1 numeric character")
            }
        }
    }

    @Test
    fun testShowErrorTextPasswordContainSpecialChar() {
        unregisterIdlingResource()
        registerScreen {
            tilPassword.edit { typeText("Test1234") }
            btnSignUp.click()
            tilPassword {
                hasError("Password must contain at least 1 special character")
            }
        }
    }

    @Test
    fun testNotShowAnyError() {
        unregisterIdlingResource()
        registerScreen {
            tilFullname.edit.typeText("a")
            tilEmail.edit.typeText(TestData.REGISTER_USER_EMAIL)
            tilPassword.edit.typeText("Tt123###")
            tilRetypePassword.edit.typeText("Tt123###")
            btnSignUp.click()
            tilRetypePassword.isErrorDisabled()
            tilPassword.isErrorDisabled()
            tilFullname.isErrorDisabled()
            tilEmail.isErrorDisabled()
        }
    }

    @Test
    fun testRegisterSuccessfully() {
        unregisterIdlingResource()
        registerScreen {
            tilFullname.edit.typeText("a")
            tilEmail.edit.typeText(TestData.REGISTER_USER_EMAIL)
            tilPassword.edit.typeText("Tt123###")
            tilRetypePassword.edit.typeText("Tt123###")
            btnSignUp.click()
            tilRetypePassword.isErrorDisabled()
            tilPassword.isErrorDisabled()
            tilFullname.isErrorDisabled()
            tilEmail.isErrorDisabled()
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