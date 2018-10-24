package network.omisego.omgwallet.extension

import co.omisego.omisego.security.OMGKeyManager
import com.crashlytics.android.Crashlytics

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun OMGKeyManager.encrypt(data: String) = try {
    this.encrypt(data.toByteArray())
} catch (e: Exception) {
    Crashlytics.log(e.message)
    data
}

fun OMGKeyManager.decrypt(data: String) = try {
    this.decrypt(data.toByteArray())
} catch (e: Exception) {
    Crashlytics.log(e.message)
    data
}