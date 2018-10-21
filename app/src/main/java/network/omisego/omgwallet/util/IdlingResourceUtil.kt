package network.omisego.omgwallet.util

import androidx.test.espresso.idling.CountingIdlingResource

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object IdlingResourceUtil {
    var idlingResource: CountingIdlingResource? = null

    fun increment() = idlingResource?.increment()

    fun decrement() = idlingResource?.decrement()
}
