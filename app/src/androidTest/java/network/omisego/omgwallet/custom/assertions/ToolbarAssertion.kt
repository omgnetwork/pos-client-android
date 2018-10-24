package network.omisego.omgwallet.custom.assertions

import androidx.test.espresso.assertion.ViewAssertions
import com.agoda.kakao.BaseAssertions
import network.omisego.omgwallet.custom.matchers.ToolbarMatcher

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

interface ToolbarAssertion : BaseAssertions {
    fun hasTitle(title: String) {
        view.check(ViewAssertions.matches(ToolbarMatcher(title)))
    }
}
