package network.omisego.omgwallet.repository

import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.ClientAuthenticationToken
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.TransactionRequestType
import co.omisego.omisego.model.User
import co.omisego.omisego.model.WalletList
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.repository.contract.BalanceDataRepository
import network.omisego.omgwallet.storage.SessionStorage
import network.omisego.omgwallet.storage.Storage
import network.omisego.omgwallet.util.Event

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class LocalRepository(
    private val storage: Storage,
    private val sessionStorage: SessionStorage
) : BalanceDataRepository {
    override fun loadWallet(liveAPIResult: MutableLiveData<Event<APIResult>>) {
        liveAPIResult.value = Event(APIResult.Success(loadWallet()))
    }

    fun clearSession() = sessionStorage.clear()

    fun saveSession(clientAuthToken: ClientAuthenticationToken) = sessionStorage.save(clientAuthToken)

    fun hasFormattedId() = storage.hasFormattedId()

    fun deleteFingerprintCredential() {
        storage.deleteFingerprintCredential()
    }

    fun hasAuthenticationToken() = storage.hasAuthenticationToken()

    fun hasFingerprintPassword() = storage.hasFingerprintCredential()

    fun loadWallet() = storage.loadWallets()

    fun loadFingerprintOption() = storage.loadFingerprintOption()

    fun loadUserEmail() = storage.loadUserEmail()

    fun loadFingerprintCredential() = storage.loadFingerprintCredential()

    fun loadTransactionRequestFormattedId() = storage.loadFormattedId()

    fun loadCredential() = storage.loadCredential()

    fun loadUser() = storage.loadUser()

    fun saveTransactionRequestFormattedId(formattedIds: Map<TransactionRequestType, String>) = storage.saveFormattedId(formattedIds)

    fun saveUser(user: User) = storage.saveUser(user)

    fun saveCredential(credential: Credential) = storage.saveCredential(credential)

    fun saveFingerprintCredential(password: String) = storage.saveFingerprintCredential(password)

    fun saveUserEmail(email: String) = storage.saveUserEmail(email)

    fun saveWallets(data: WalletList) = storage.saveWallets(data)

    fun saveFingerprintOption(checked: Boolean) {
        storage.saveFingerprintOption(checked)
    }

    fun saveTokenPrimary(token: Token) {
        storage.saveTokenPrimary(token)
    }

    fun loadTokenPrimary(): String? = storage.loadTokenPrimary()
}
