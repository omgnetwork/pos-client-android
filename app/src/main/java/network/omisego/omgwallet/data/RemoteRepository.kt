package network.omisego.omgwallet.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.params.LoginParams
import co.omisego.omisego.model.params.SignUpParams
import co.omisego.omisego.model.transaction.list.TransactionListParams
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestCreateParams
import network.omisego.omgwallet.data.contract.BalanceDataRepository
import network.omisego.omgwallet.extension.subscribe
import network.omisego.omgwallet.extension.subscribeSingleEvent
import network.omisego.omgwallet.livedata.Event
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.network.ClientProvider
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

    override fun loadWallet(liveAPIResult: MutableLiveData<Event<APIResult>>, networkOnly: Boolean) {
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
}
