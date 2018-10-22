package network.omisego.omgwallet.util

import androidx.test.espresso.idling.CountingIdlingResource

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object IdlingResourceUtil {
    val idlingResource: CountingIdlingResource by lazy {
        CountingIdlingResource("coroutines", true)
    }
}
