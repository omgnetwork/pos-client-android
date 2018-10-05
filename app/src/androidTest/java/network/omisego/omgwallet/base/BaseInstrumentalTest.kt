package network.omisego.omgwallet.base

import android.content.Context
import androidx.test.InstrumentationRegistry
import network.omisego.omgwallet.R

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
open class BaseInstrumentalTest {

    fun clearSharePreference() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}