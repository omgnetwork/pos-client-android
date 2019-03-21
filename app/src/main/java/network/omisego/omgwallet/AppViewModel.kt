package network.omisego.omgwallet

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.omisego.omisego.OMGAPIClient
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.ClientAuthenticationToken
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.websocket.SocketClientContract
import network.omisego.omgwallet.extension.logi
import network.omisego.omgwallet.listener.LoginListener
import network.omisego.omgwallet.livedata.SocketStateLiveData
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.repository.LocalRepository
import network.omisego.omgwallet.repository.RemoteRepository

class AppViewModel(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel(), LoginListener {
    private val liveAuthenticationToken: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val liveClient: MutableLiveData<OMGAPIClient> by lazy { MutableLiveData<OMGAPIClient>() }
    private val liveSocket: MutableLiveData<SocketClientContract.Client> by lazy { MutableLiveData<SocketClientContract.Client>() }
    val liveSocketState: SocketStateLiveData by lazy { SocketStateLiveData(liveAuthenticationToken, liveSocket) }
    val liveConsumptionRequestEvent: MutableLiveData<TransactionConsumption> by lazy { MutableLiveData<TransactionConsumption>() }
    val liveConsumptionRequestFailEvent: MutableLiveData<APIError> by lazy { MutableLiveData<APIError>() }
    val liveConsumptionFinalizedEvent: MutableLiveData<TransactionConsumption> by lazy { MutableLiveData<TransactionConsumption>() }
    val liveConsumptionFinalizedFailEvent: MutableLiveData<APIError> by lazy { MutableLiveData<APIError>() }

    fun setAuthenticationToken(authenticationToken: String?) {
        localRepository.saveCredential(Credential(authenticationToken))
        liveAuthenticationToken.value = authenticationToken
    }

    fun loadAuthenticationToken(): String? {
        return localRepository.loadCredential().authenticationToken
    }

    fun setClient(client: OMGAPIClient) {
        liveClient.value = client
    }

    fun setSocketClient(socketClient: SocketClientContract.Client?) {
        liveSocket.value = socketClient
    }

    fun startListenForUserEvent() {
        logi("start listen for user event...")
        liveSocket.value?.let {
            remoteRepository.listenUserSocketEvent(
                it,
                liveConsumptionRequestEvent,
                liveConsumptionRequestFailEvent,
                liveConsumptionFinalizedEvent,
                liveConsumptionFinalizedFailEvent
            )
        }
    }

    fun stopListenForUserEvent() {
        liveSocket.value?.let {
            remoteRepository.stopListeningToUserSocketEvent(it)
        }
    }

    // ****** Implement LoginListener ******

    override fun onLoggedin(clientAuthToken: ClientAuthenticationToken) {
        localRepository.saveSession(clientAuthToken)
        setAuthenticationToken(clientAuthToken.authenticationToken)
    }

    override fun onLoggedout() {
        localRepository.clearSession()
        setAuthenticationToken(null)
    }
}