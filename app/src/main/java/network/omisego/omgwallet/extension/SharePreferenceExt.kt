package network.omisego.omgwallet.extension

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.SharedPreferences

operator fun SharedPreferences.set(key: String, value: String) {
    this.edit().putString(key, value).apply()
}

operator fun SharedPreferences.get(key: String) = this.getString(key, "")