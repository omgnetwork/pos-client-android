package network.omisego.omgwallet.extension

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.SharedPreferences
import network.omisego.omgwallet.storage.StorageKey

operator fun SharedPreferences.set(key: String, value: String) {
    this.edit().putString(key, value).apply()
}

operator fun SharedPreferences.get(key: String) = this.getString(key, "")

operator fun SharedPreferences.set(storageKey: StorageKey, value: String) {
    this.edit().putString(storageKey.value, value).apply()
}

operator fun SharedPreferences.get(storageKey: StorageKey) = this.getString(storageKey.value, "")

fun SharedPreferences.contains(storageKey: StorageKey) = this.contains(storageKey.value)

fun SharedPreferences.Editor.remove(storageKey: StorageKey) = this.remove(storageKey.value)

fun SharedPreferences.Editor.putBoolean(storageKey: StorageKey, value: Boolean) = this.putBoolean(storageKey.value, value)

fun SharedPreferences.getBoolean(storageKey: StorageKey, defValue: Boolean) = this.getBoolean(storageKey.value, defValue)
