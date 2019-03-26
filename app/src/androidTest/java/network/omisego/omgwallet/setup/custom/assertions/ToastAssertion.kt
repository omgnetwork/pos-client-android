package network.omisego.omgwallet.setup.custom.assertions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import network.omisego.omgwallet.setup.base.BaseInstrumentalTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not

fun BaseInstrumentalTest.onToast(msg: String) {
    val decorView = rule.activity.window.decorView
    val visibleAssertion = matches(isDisplayed())
    val toastParentMatcher = withDecorView(not(`is`(decorView)))
    val toastMatcher = withText(msg)
    onView(toastMatcher).inRoot(toastParentMatcher).check(visibleAssertion)
}