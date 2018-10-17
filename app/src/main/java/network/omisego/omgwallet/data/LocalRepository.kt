package network.omisego.omgwallet.data

import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.User
import co.omisego.omisego.model.WalletList
import network.omisego.omgwallet.data.contract.BalanceDataRepository
import network.omisego.omgwallet.livedata.Event
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.storage.Storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class LocalRepository : BalanceDataRepository {
    override fun loadWallet(liveAPIResult: MutableLiveData<Event<APIResult>>, networkOnly: Boolean) {
        liveAPIResult.value = Event(APIResult.Success(Storage.loadWallets()))
    }

    fun clearSession() = Storage.clearSession()

    fun clearOldAccountCache(email: String) = Storage.clearOldAccountCache(email)

    fun deleteFingerprintCredential() {
        Storage.deleteFingerprintCredential()
    }

    fun hasAuthenticationToken() = Storage.hasAuthenticationToken()

    fun hasFingerprintPassword() = Storage.hasFingerprintCredential()

    fun loadWallet() = Storage.loadWallets()

    fun loadFingerprintOption() = Storage.loadFingerprintOption()

    fun loadUserEmail() = Storage.loadUserEmail()

    fun loadFingerprintCredential() = Storage.loadFingerprintCredential()

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
