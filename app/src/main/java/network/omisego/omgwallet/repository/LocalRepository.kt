package network.omisego.omgwallet.repository

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.ClientAuthenticationToken
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.TransactionRequestType
import co.omisego.omisego.model.User
import co.omisego.omisego.model.WalletList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.repository.contract.BalanceDataRepository
import network.omisego.omgwallet.storage.SessionStorage
import network.omisego.omgwallet.storage.Storage
import network.omisego.omgwallet.storage.StorageKey.KEY_AUTHENTICATION_TOKEN
import network.omisego.omgwallet.storage.StorageKey.KEY_FINGERPRINT_OPTION
import network.omisego.omgwallet.storage.StorageKey.KEY_FINGERPRINT_USER_PASSWORD
import network.omisego.omgwallet.storage.StorageKey.KEY_TOKEN_PRIMARY
import network.omisego.omgwallet.storage.StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE
import network.omisego.omgwallet.storage.StorageKey.KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND
import network.omisego.omgwallet.storage.StorageKey.KEY_USER
import network.omisego.omgwallet.storage.StorageKey.KEY_USER_EMAIL
import network.omisego.omgwallet.storage.StorageKey.KEY_WALLET
import network.omisego.omgwallet.util.Event

class LocalRepository(
    private val storage: Storage,
    private val sessionStorage: SessionStorage
) : BalanceDataRepository {
    override fun loadWallet(liveAPIResult: MutableLiveData<Event<APIResult>>) {
        liveAPIResult.value = Event(APIResult.Success(loadWallets()))
    }

    fun deleteAll() =
        storage.deleteAll()

    fun deleteSession() =
        sessionStorage.delete()

    fun deleteFingerprintSession() =
        sessionStorage.deleteFingerprint()

    fun deleteTransactionRequest() =
        storage.deleteRecords(KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND, KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE)

    fun deleteTokenPrimary() =
        storage.deleteRecords(KEY_TOKEN_PRIMARY)

    fun deleteWallets() =
        storage.deleteRecords(KEY_WALLET)

    fun hasFormattedId() =
        storage.has(KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND, KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE)

    fun hasAuthenticationToken() =
        storage.has(KEY_AUTHENTICATION_TOKEN)

    fun hasFingerprintPassword() =
        storage.has(KEY_FINGERPRINT_USER_PASSWORD)

    fun hasUser() =
        storage.has(KEY_USER)

    fun hasWallet() =
        storage.has(KEY_WALLET)

    fun loadWallets() =
        storage.getRecord<WalletList>(KEY_WALLET)

    fun loadFingerprintOption() =
        storage.getBooleanRecord(KEY_FINGERPRINT_OPTION)

    fun loadUserEmail() =
        storage.getStringRecord(KEY_USER_EMAIL)

    fun loadFingerprintCredential(): String? {
        val password = storage.getStringRecord(KEY_FINGERPRINT_USER_PASSWORD) ?: return null
        return storage.decrypt(password)
    }

    /* Returns send|receive */
    fun loadTransactionRequest() =
        arrayOf(
            storage.getStringRecord(KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE),
            storage.getStringRecord(KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND)
        ).joinToString("|")

    fun loadCredential() =
        storage.loadCredential()

    fun loadUser() =
        storage.getRecord<User>(KEY_USER)

    fun loadTokenPrimary(): String? =
        storage.getRecord(KEY_TOKEN_PRIMARY)

    fun saveSession(clientAuthToken: ClientAuthenticationToken) =
        sessionStorage.save(clientAuthToken)

    fun saveFingerprintSessionAsync(password: String, enabled: Boolean) =
        GlobalScope.async {
            sessionStorage.saveFingerprint(password, enabled)
        }

    fun saveTransactionRequest(formattedIds: Map<TransactionRequestType, String>) =
        storage.putRecords(
            KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND to formattedIds[TransactionRequestType.SEND],
            KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE to formattedIds[TransactionRequestType.RECEIVE]
        )

    fun saveUser(user: User) =
        storage.putRecords(KEY_USER to storage.toJson(user))

    fun saveCredential(credential: Credential) =
        storage.putRecords(KEY_AUTHENTICATION_TOKEN to credential.authenticationToken)

    fun saveWallets(data: WalletList) =
        storage.putRecords(KEY_WALLET to storage.toJson(data))

    fun saveTokenPrimary(token: Token) =
        storage.putRecords(KEY_TOKEN_PRIMARY to token.id)
}
