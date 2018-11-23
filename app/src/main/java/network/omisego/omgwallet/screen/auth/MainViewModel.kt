package network.omisego.omgwallet.screen.auth

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.TransactionConsumption
import network.omisego.omgwallet.GraphMainDirections
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.extension.logi

class MainViewModel(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    val liveConsumptionRequestEvent: MutableLiveData<TransactionConsumption> by lazy { MutableLiveData<TransactionConsumption>() }
    val liveConsumptionRequestFailEvent: MutableLiveData<APIError> by lazy { MutableLiveData<APIError>() }
    val liveConsumptionFinalizedEvent: MutableLiveData<TransactionConsumption> by lazy { MutableLiveData<TransactionConsumption>() }
    val liveConsumptionFinalizedFailEvent: MutableLiveData<APIError> by lazy { MutableLiveData<APIError>() }

    fun hasTransactionRequestFormattedId(): Boolean {
        return localRepository.hasFormattedId()
    }

    fun loadPrimaryTokenId(): String? {
        return localRepository.loadTokenPrimary()
    }

    fun provideSplashDirection() = GraphMainDirections
        .actionGlobalSplash()
        .setPrimaryTokenId(loadPrimaryTokenId())

    fun startListenForUserEvent() {
        logi("start listen for user event...")
        remoteRepository.listenUserSocketEvent(
            liveConsumptionRequestEvent,
            liveConsumptionRequestFailEvent,
            liveConsumptionFinalizedEvent,
            liveConsumptionFinalizedFailEvent
        )
    }

    fun stopListenForUserEvent() {
        remoteRepository.stopListeningToUserSocketEvent()
    }
}
