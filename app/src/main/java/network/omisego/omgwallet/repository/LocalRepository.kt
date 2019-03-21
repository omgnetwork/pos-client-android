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
    private val sessionStorage: SessionStorage
) : BalanceDataRepository {
    override fun loadWallet(liveAPIResult: MutableLiveData<Event<APIResult>>) {
        liveAPIResult.value = Event(APIResult.Success(loadWallet()))
    }

    fun clearSession() = sessionStorage.clear()

    fun saveSession(clientAuthToken: ClientAuthenticationToken) = sessionStorage.save(clientAuthToken)

    fun hasFormattedId() = Storage.hasFormattedId()

    fun deleteFingerprintCredential() {
        Storage.deleteFingerprintCredential()
    }

    fun hasAuthenticationToken() = Storage.hasAuthenticationToken()

    fun hasFingerprintPassword() = Storage.hasFingerprintCredential()

    fun loadWallet() = Storage.loadWallets()

    fun loadFingerprintOption() = Storage.loadFingerprintOption()

    fun loadUserEmail() = Storage.loadUserEmail()

    fun loadFingerprintCredential() = Storage.loadFingerprintCredential()

    fun loadTransactionRequestFormattedId() = Storage.loadFormattedId()

    fun loadCredential() = Storage.loadCredential()

    fun loadUser() = Storage.loadUser()

    fun saveTransactionRequestFormattedId(formattedIds: Map<TransactionRequestType, String>) = Storage.saveFormattedId(formattedIds)

    fun saveUser(user: User) = Storage.saveUser(user)

    fun saveCredential(credential: Credential) = Storage.saveCredential(credential)

    fun saveFingerprintCredential(password: String) = Storage.saveFingerprintCredential(password)

    fun saveUserEmail(email: String) = Storage.saveUserEmail(email)

    fun saveWallets(data: WalletList) = Storage.saveWallets(data)

    fun saveFingerprintOption(checked: Boolean) {
        Storage.saveFingerprintOption(checked)
    }

    fun saveTokenPrimary(token: Token) {
        Storage.saveTokenPrimary(token)
    }

    fun loadTokenPrimary(): String? = Storage.loadTokenPrimary()
}
