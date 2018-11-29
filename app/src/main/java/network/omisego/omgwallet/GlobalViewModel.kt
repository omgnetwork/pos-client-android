package network.omisego.omgwallet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.websocket.SocketClientContract
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.extension.logi

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/11/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class GlobalViewModel(val remoteRepository: RemoteRepository) : ViewModel() {
    val liveConsumptionRequestEvent: MutableLiveData<TransactionConsumption> by lazy { MutableLiveData<TransactionConsumption>() }
    val liveConsumptionRequestFailEvent: MutableLiveData<APIError> by lazy { MutableLiveData<APIError>() }
    val liveConsumptionFinalizedEvent: MutableLiveData<TransactionConsumption> by lazy { MutableLiveData<TransactionConsumption>() }
    val liveConsumptionFinalizedFailEvent: MutableLiveData<APIError> by lazy { MutableLiveData<APIError>() }
    val liveAuthenticationToken: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun startListenForUserEvent(socketClient: SocketClientContract.Client) {
        logi("start listen for user event...")
        remoteRepository.listenUserSocketEvent(
            socketClient,
            liveConsumptionRequestEvent,
            liveConsumptionRequestFailEvent,
            liveConsumptionFinalizedEvent,
            liveConsumptionFinalizedFailEvent
        )
    }

    fun stopListenForUserEvent(socketClient: SocketClientContract.Client) {
        remoteRepository.stopListeningToUserSocketEvent(socketClient)
    }
}