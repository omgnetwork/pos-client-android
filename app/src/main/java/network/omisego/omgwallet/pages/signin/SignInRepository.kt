package network.omisego.omgwallet.pages.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.User
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgwallet.extensions.subscribe
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.storage.Storage

class SignInRepository {
    fun signIn(params: LoginParams, liveAPIResult: MutableLiveData<APIResult>): LiveData<APIResult> {
        return ClientProvider.client.login(params).subscribe(liveAPIResult)
    }

    fun loadFingerprintOption() = Storage.loadFingerprintOption()

    fun loadUserEmail() = Storage.loadUserEmail()

    fun loadFingerprintCredential() = Storage.loadFingerprintCredential()

    fun saveUser(user: User) = Storage.saveUser(user)

    fun saveCredential(credential: Credential) = Storage.saveCredential(credential)

    fun saveUserEmail(email: String) = Storage.saveUserEmail(email)
}
