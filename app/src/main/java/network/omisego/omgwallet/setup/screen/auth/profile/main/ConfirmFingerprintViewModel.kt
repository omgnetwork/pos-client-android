package network.omisego.omgwallet.screen.auth.profile.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.omisego.omisego.model.params.LoginParams
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.repository.LocalRepository
import network.omisego.omgwallet.repository.RemoteRepository
import network.omisego.omgwallet.util.Event

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ConfirmFingerprintViewModel(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    val liveAPIResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    fun signIn(password: String) {
        remoteRepository.signIn(LoginParams(
            localRepository.loadUserEmail()!!,
            password
        ), liveAPIResult)
    }

    fun enableFingerprint(password: String) {
        localRepository.saveFingerprintSessionAsync(password, true)
    }
}