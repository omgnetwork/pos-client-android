package network.omisego.omgwallet.storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.content.SharedPreferences
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.User
import co.omisego.omisego.model.WalletList
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestType
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

    fun hasAuthenticationToken() = sharePref.contains(StorageKey.KEY_AUTHENTICATION_TOKEN)

    fun saveCredential(credential: Credential) {
        sharePref[StorageKey.KEY_AUTHENTICATION_TOKEN] = (credential.authenticationToken ?: "") encryptWith keyManager
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

    fun saveWallets(wallet: WalletList) {
        sharePref[StorageKey.KEY_WALLET] = gson.toJson(wallet)
    }

    fun loadWallets(): WalletList? {
        if (sharePref[StorageKey.KEY_WALLET].isNullOrEmpty()) return null
        return gson.fromJson<WalletList>(sharePref[StorageKey.KEY_WALLET], WalletList::class.java)
    }

    fun saveUser(user: User) {
        sharePref[StorageKey.KEY_USER] = gson.toJson(user)
    }

    fun loadUser(): User? {
        if (sharePref[StorageKey.KEY_USER].isNullOrEmpty()) return null
        return gson.fromJson<User>(sharePref[StorageKey.KEY_USER], User::class.java)
    }

    fun saveTokenPrimary(token: Token) {
        sharePref[StorageKey.KEY_TOKEN_PRIMARY] = token.id
    }

    fun loadTokenPrimary(): String? {
        if (sharePref[StorageKey.KEY_TOKEN_PRIMARY].isNullOrEmpty()) return null
        return sharePref[StorageKey.KEY_TOKEN_PRIMARY]
    }

    fun hasFormattedId() = sharePref.contains(StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE)
        && sharePref.contains(StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND)

    fun saveFormattedId(request: TransactionRequest) {
        when (request.type) {
            TransactionRequestType.SEND ->
                sharePref[StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND] = request.formattedId
            TransactionRequestType.RECEIVE ->
                sharePref[StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE] = request.formattedId
        }
    }

    /* Returns send|receive */
    fun loadFormattedId(): String =
        "${sharePref[StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND]}|${sharePref[StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE]}"

    fun clearSession() {
        sharePref.edit()
            .remove(StorageKey.KEY_AUTHENTICATION_TOKEN)
            .remove(StorageKey.KEY_WALLET)
            .remove(StorageKey.KEY_USER)
            .remove(StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE)
            .remove(StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND)
            .apply()
    }

    fun clearOldAccountCache(email: String) {
        if (email != sharePref[StorageKey.KEY_USER_EMAIL]) {
            sharePref.edit()
                .remove(StorageKey.KEY_TOKEN_PRIMARY)
                .remove(StorageKey.KEY_WALLET)
                .remove(StorageKey.KEY_FINGERPRINT_USER_PASSWORD)
                .remove(StorageKey.KEY_FINGERPRINT_OPTION)
                .apply()
        }
    }
}
