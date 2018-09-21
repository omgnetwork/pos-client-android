package network.omisego.omgwallet.storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.content.SharedPreferences
import co.omisego.omisego.model.User
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.security.OMGKeyManager
import co.omisego.omisego.utils.GsonProvider
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import network.omisego.omgwallet.BuildConfig
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.decryptWith
import network.omisego.omgwallet.extension.encryptWith
import network.omisego.omgwallet.extension.get
import network.omisego.omgwallet.extension.set
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.util.ContextUtil.context

object Storage {
    private val gson by lazy { GsonProvider.create() }
    private val sharePref: SharedPreferences by lazy {
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    private val keyManager: OMGKeyManager by lazy {
        OMGKeyManager.Builder {
            keyAlias = BuildConfig.CLIENT_ENCRYPT_KEY_ALIAS
            iv = BuildConfig.CLIENT_ENCRYPT_IV
        }.build(context)
    }

    fun saveCredential(credential: Credential): Deferred<Unit> {
        return async {
            sharePref[StorageKey.KEY_AUTHENTICATION_TOKEN] = credential.authenticationToken ?: "" encryptWith keyManager
        }
    }

    fun loadCredential(): Credential {
        val authenticationToken = sharePref[StorageKey.KEY_AUTHENTICATION_TOKEN]!!
        if (authenticationToken.isEmpty()) {
            return Credential()
        }
        return Credential(
            authenticationToken decryptWith keyManager
        )
    }

    fun saveUserEmail(email: String) {
        sharePref[StorageKey.KEY_USER_EMAIL] = email
    }

    fun loadUserEmail() = sharePref[StorageKey.KEY_USER_EMAIL]

    fun saveFingerprintCredential(password: String): Deferred<Unit> {
        return async {
            sharePref[StorageKey.KEY_FINGERPRINT_USER_PASSWORD] = password encryptWith keyManager
        }
    }

    fun loadFingerprintCredential() = sharePref[StorageKey.KEY_FINGERPRINT_USER_PASSWORD] decryptWith keyManager

    fun hasFingerprintCredential() = sharePref.contains(StorageKey.KEY_FINGERPRINT_USER_PASSWORD)

    fun deleteFingerprintCredential() {
        sharePref.edit()
            .remove(StorageKey.KEY_FINGERPRINT_USER_PASSWORD)
            .apply()
    }

    fun saveFingerprintOption(checked: Boolean) {
        sharePref.edit().putBoolean(StorageKey.KEY_FINGERPRINT_OPTION, checked).apply()
    }

    fun loadFingerprintOption(): Boolean {
        return sharePref.getBoolean(StorageKey.KEY_FINGERPRINT_OPTION, false)
    }

    fun saveWallet(wallet: Wallet) {
        sharePref[StorageKey.KEY_WALLET] = gson.toJson(wallet)
    }

    fun loadWallet(): Wallet? {
        if (sharePref[StorageKey.KEY_WALLET].isNullOrEmpty()) return null
        return gson.fromJson<Wallet>(sharePref[StorageKey.KEY_WALLET], Wallet::class.java)
    }

    fun saveUser(user: User) {
        sharePref[StorageKey.KEY_USER] = gson.toJson(user)
    }

    fun loadUser(): User? {
        if (sharePref[StorageKey.KEY_USER].isNullOrEmpty()) return null
        return gson.fromJson<User>(sharePref[StorageKey.KEY_USER], User::class.java)
    }

    fun clearEverything() {
        sharePref.edit()
            .remove(StorageKey.KEY_API_KEY)
            .remove(StorageKey.KEY_AUTHENTICATION_TOKEN)
            .remove(StorageKey.KEY_WALLET)
            .remove(StorageKey.KEY_USER)
            .apply()
    }
}
