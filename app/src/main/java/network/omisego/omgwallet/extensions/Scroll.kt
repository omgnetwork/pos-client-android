package network.omisego.omgwallet.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.R

fun AppCompatActivity.scrollBottom() {
    if (this is MainActivity) {
        val scroller = this.findViewById<NestedScrollView>(R.id.nestedScrollView)
        scroller?.scrollTo(0, scroller.bottom)
    }
}

fun Fragment.scrollBottom() {
    val hostActivity = activity
    if (hostActivity is AppCompatActivity) {
        hostActivity.scrollBottom()
    }
}
