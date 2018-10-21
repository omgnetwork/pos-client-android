package network.omisego.omgwallet.screen.auth.splash

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.WalletList
import co.omisego.omisego.model.transaction.request.TransactionRequestCreateParams
import co.omisego.omisego.model.transaction.request.TransactionRequestType
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import network.omisego.omgwallet.R
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.extension.either
import network.omisego.omgwallet.extension.logi
import network.omisego.omgwallet.livedata.Event
import network.omisego.omgwallet.model.APIResult

class PreloadResourceViewModel(
    private val app: Application,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : AndroidViewModel(app) {
    val liveResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }
    val liveTransactionRequestFormattedId: MutableLiveData<Event<String>> by lazy { MutableLiveData<Event<String>>() }
    val liveCreateTransactionRequestFailed: MutableLiveData<Event<APIError>> by lazy { MutableLiveData<Event<APIError>>() }
    val liveStatus: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveCloseButtonVisiblity: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun loadWallets() {
        liveCloseButtonVisiblity.value = View.GONE
        remoteRepository.loadWallet(liveResult, true)
        liveStatus.value = app.getString(R.string.splash_status_loading_wallet)
    }

    fun saveWallet(data: WalletList) {
        localRepository.saveWallets(data)
    }

    fun createTransactionRequest(walletList: WalletList) {
        liveStatus.value = app.getString(R.string.splash_status_creating_transaction)

        val primaryToken = localRepository.loadTokenPrimary()
        val selectedTokenId = walletList.data[0].balances.findLast { it.token.id == primaryToken }?.token?.id
            ?: walletList.data[0].balances[0].token.id
        val params = TransactionRequestCreateParams(
            TransactionRequestType.RECEIVE,
            selectedTokenId,
            requireConfirmation = false
        )
        val formattedIds: MutableList<String> = mutableListOf()
        launch(UI) {
            val result = async {
                val txReceiveResult = remoteRepository.createTransactionRequest(params)
                val txSendResult = remoteRepository.createTransactionRequest(
                    params.copy(type = TransactionRequestType.SEND, requireConfirmation = true)
                )
                return@async txReceiveResult to txSendResult
            }
            val (txReceive, txSend) = result.await()
            txReceive.either().either({ formattedIds.add(it.data.formattedId) }, this@PreloadResourceViewModel::handleAPIError)
            txSend.either().either({ formattedIds.add(it.data.formattedId) }, this@PreloadResourceViewModel::handleAPIError)

            if (formattedIds.size == 2) {
                val message = "${formattedIds[0]}|${formattedIds[1]}"
                logi(message)

                /* Save into local storage */
                localRepository.saveTransactionRequestFormattedId(txReceive.body()?.data!!)
                localRepository.saveTransactionRequestFormattedId(txSend.body()?.data!!)

                /* Emit event success */
                liveTransactionRequestFormattedId.value = Event(message)
            }
        }
    }

    fun handleAPIError(apiError: APIError) {
        liveCreateTransactionRequestFailed.value = Event(apiError)
        liveCloseButtonVisiblity.value = View.VISIBLE
    }
}
