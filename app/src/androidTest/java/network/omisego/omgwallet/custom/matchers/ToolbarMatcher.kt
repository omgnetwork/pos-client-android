package network.omisego.omgwallet.custom.matchers

import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ToolbarMatcher(
    private val title: String
) : BoundedMatcher<Any, Toolbar>(Toolbar::class.java) {
    override fun describeTo(description: Description?) {
        description?.appendText("with toolbar title: $title")
    }

    override fun matchesSafely(toolbar: Toolbar?): Boolean {
        return title == toolbar?.title
    }
}
