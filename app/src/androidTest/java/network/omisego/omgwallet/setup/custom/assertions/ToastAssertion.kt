package network.omisego.omgwallet.setup.custom.assertions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import network.omisego.omgwallet.setup.base.BaseInstrumentalTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

fun BaseInstrumentalTest.onToast(matcher: Matcher<View>) {
    val decorView = rule.activity.window.decorView
    val visibleAssertion = matches(isDisplayed())
    val toastParentMatcher = withDecorView(not(`is`(decorView)))
    onView(matcher).inRoot(toastParentMatcher).check(visibleAssertion)
}