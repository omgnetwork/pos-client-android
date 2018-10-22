package network.omisego.omgwallet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.params.LoginParams
import co.omisego.omisego.model.params.SignUpParams
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionActionParams
import co.omisego.omisego.model.transaction.list.TransactionListParams
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestCreateParams
import co.omisego.omisego.operation.startListeningEvents
import co.omisego.omisego.websocket.event.TransactionConsumptionFinalizedEvent
import co.omisego.omisego.websocket.event.TransactionConsumptionRequestEvent
import co.omisego.omisego.websocket.listener.SocketCustomEventListener
import network.omisego.omgwallet.data.contract.BalanceDataRepository
import network.omisego.omgwallet.extension.subscribe
import network.omisego.omgwallet.extension.subscribeSingleEvent
import network.omisego.omgwallet.livedata.Event
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.storage.Storage
import retrofit2.Response

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class RemoteRepository : BalanceDataRepository {
    fun createTransactionRequest(params: TransactionRequestCreateParams): Response<OMGResponse<TransactionRequest>> {
        return ClientProvider.client.createTransactionRequest(params).execute()
    }

    override fun loadWallet(liveAPIResult: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client.getWallets().subscribeSingleEvent(liveAPIResult)
    }

    fun signIn(params: LoginParams, liveAPIResult: MutableLiveData<Event<APIResult>>): LiveData<Event<APIResult>> {
        return ClientProvider.client.login(params).subscribeSingleEvent(liveAPIResult)
    }

    fun signup(params: SignUpParams, liveAPIResult: MutableLiveData<APIResult>) {
        ClientProvider.client.signup(params).subscribe(liveAPIResult)
    }

    fun getTransactions(params: TransactionListParams, liveAPIResult: MutableLiveData<APIResult>) {
        ClientProvider.client.getTransactions(params).subscribe(liveAPIResult)
    }

    fun approveTransaction(id: String, liveAPIResult: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client
            .approveTransactionConsumption(TransactionConsumptionActionParams(id))
            .subscribeSingleEvent(liveAPIResult)
    }

    fun rejectTransaction(id: String, liveAPIResult: MutableLiveData<Event<APIResult>>) {
        ClientProvider.client
            .rejectTransactionConsumption(TransactionConsumptionActionParams(id))
            .subscribeSingleEvent(liveAPIResult)
    }

    fun listenUserSocketEvent(
        liveConsumptionRequestEvent: MutableLiveData<TransactionConsumption>,
        liveConsumptionRequestFailEvent: MutableLiveData<APIError>,
        liveConsumptionFinalizedEvent: MutableLiveData<TransactionConsumption>,
        liveConsumptionFinalizedFailEvent: MutableLiveData<APIError>
    ) {
        /* Listen for request event */
        val user = Storage.loadUser()
        user?.startListeningEvents(
            ClientProvider.socketClient,
            listener = SocketCustomEventListener.forEvent<TransactionConsumptionRequestEvent> {
                /* TODO: Handle incoming transaction consumption request event here*/
                // Show confirmation fragment here
                val txConsumption = it.socketReceive
                if (txConsumption.error == null) {
                    liveConsumptionRequestEvent.value = txConsumption.data
                } else {
                    liveConsumptionRequestFailEvent.value = txConsumption.error
                }
            })
        user?.startListeningEvents(
            ClientProvider.socketClient,
            listener = SocketCustomEventListener.forEvent<TransactionConsumptionFinalizedEvent> {
                /* TODO: Handle transaction consumption finalized event here */
                val txConsumption = it.socketReceive
                if (txConsumption.error == null) {
                    liveConsumptionFinalizedEvent.value = txConsumption.data
                } else {
                    liveConsumptionFinalizedFailEvent.value = txConsumption.error
                }
            })
    }

    fun stopListeningToUserSocketEvent() {
        val user = Storage.loadUser()
        user?.stopListening(ClientProvider.socketClient)
    }
}
