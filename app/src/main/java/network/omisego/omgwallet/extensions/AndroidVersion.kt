package network.omisego.omgwallet.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Build

inline fun runOnM(lambda: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        lambda()
    }
}

inline fun runBelowM(lambda: () -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        lambda()
    }
}

inline fun runOnP(lambda: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        lambda()
    }
}

inline fun runOnMToP(lambda: () -> Unit) {
    if (Build.VERSION.SDK_INT in Build.VERSION_CODES.M until Build.VERSION_CODES.P) {
        lambda()
    }
}

