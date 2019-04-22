package network.omisego.omgwallet.storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.model.ClientAuthenticationToken

class SessionStorage(val storage: Storage) {
    fun delete() {
        storage.deleteRecords(
            StorageKey.KEY_AUTHENTICATION_TOKEN,
            StorageKey.KEY_WALLET,
            StorageKey.KEY_USER,
            StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE,
            StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND
        )
    }

    fun save(clientAuthToken: ClientAuthenticationToken) {
        clearCacheIfNeeded(clientAuthToken.user.email)
        storage.putRecords(
            StorageKey.KEY_USER_EMAIL to clientAuthToken.user.email,
            StorageKey.KEY_USER to storage.toJson(clientAuthToken.user),
            StorageKey.KEY_AUTHENTICATION_TOKEN to storage.encrypt(clientAuthToken.authenticationToken)
        )
    }

    fun saveFingerprint(password: String, enabled: Boolean) {
        storage.putRecords(StorageKey.KEY_FINGERPRINT_USER_PASSWORD to storage.encrypt(password))
        storage.putBooleanRecord(StorageKey.KEY_FINGERPRINT_OPTION to enabled)
    }

    fun deleteFingerprint() {
        storage.deleteRecords(StorageKey.KEY_FINGERPRINT_USER_PASSWORD, StorageKey.KEY_FINGERPRINT_OPTION)
    }

    private fun clearCacheIfNeeded(email: String) {
        if (storage.getStringRecord(StorageKey.KEY_USER_EMAIL) == email) return
        storage.deleteRecords(
            StorageKey.KEY_TOKEN_PRIMARY,
            StorageKey.KEY_WALLET,
            StorageKey.KEY_FINGERPRINT_USER_PASSWORD,
            StorageKey.KEY_FINGERPRINT_OPTION
        )
    }
}
