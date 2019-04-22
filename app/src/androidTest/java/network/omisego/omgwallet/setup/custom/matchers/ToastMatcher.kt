package network.omisego.omgwallet.setup.custom.matchers

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.not

object ToastMatcher {
    fun notContainsNull() = withText(not(containsString("null")))
}
