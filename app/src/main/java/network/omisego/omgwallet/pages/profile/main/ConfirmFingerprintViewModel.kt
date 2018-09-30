package network.omisego.omgwallet.pages.profile.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.omisego.omisego.model.ClientAuthenticationToken
import co.omisego.omisego.model.params.LoginParams
import kotlinx.coroutines.experimental.Deferred
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.model.Credential

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ConfirmFingerprintViewModel(
    val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository
) : ViewModel() {
    val liveAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    fun signIn(password: String) {
        remoteRepository.signIn(LoginParams(
            localRepository.loadUserEmail(),
            password
        ), liveAPIResult)
    }

    fun saveCredential(data: ClientAuthenticationToken): Deferred<Unit> {
        localRepository.saveUser(data.user)
        return localRepository.saveCredential(Credential(
            data.authenticationToken
        ))
    }

    fun saveUserPassword(password: String) {
        localRepository.saveFingerprintCredential(password)
    }
}