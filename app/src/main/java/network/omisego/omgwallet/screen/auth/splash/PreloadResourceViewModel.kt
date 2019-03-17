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
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.TransactionRequestType
import co.omisego.omisego.model.WalletList
import co.omisego.omisego.model.params.client.TransactionRequestCreateParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import network.omisego.omgwallet.R
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.extension.either
import network.omisego.omgwallet.extension.logi
import network.omisego.omgwallet.livedata.Event
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.util.IdlingResourceUtil
import java.math.BigDecimal

class PreloadResourceViewModel(
    private val app: Application,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : AndroidViewModel(app) {
    val liveResult by lazy { MutableLiveData<Event<APIResult>>() }
    val liveTransactionRequestPrimaryTokenId by lazy { MutableLiveData<Event<String>>() }
    val liveCreateTransactionRequestFailed by lazy { MutableLiveData<Event<APIError>>() }
    val liveStatus by lazy { MutableLiveData<String>() }
    val liveCloseButtonVisibility by lazy { MutableLiveData<Int>() }

    fun displayTokenPrimaryNotify(balance: Balance?): String {
        return app.getString(
            R.string.balance_detail_primary_token_set_notify,
            balance?.token?.symbol
        )
    }

    fun loadBalances() = localRepository.loadWallet()?.data?.get(0)?.balances!!

    fun loadWalletLocally() {
        liveCloseButtonVisibility.value = View.GONE
        localRepository.loadWallet(liveResult)
    }

    fun loadWallets() {
        liveCloseButtonVisibility.value = View.GONE
        remoteRepository.loadWallet(liveResult)
        liveStatus.value = app.getString(R.string.splash_status_loading_wallet)
    }

    fun saveWallet(data: WalletList) {
        localRepository.saveWallets(data)
    }

    fun createTransactionRequest(walletList: WalletList, primaryTokenId: String?) {
        /* Update splash loading message */
        liveStatus.value = app.getString(R.string.splash_status_creating_transaction)

        /* Find the token to be used for send or receive */
        val selectedToken = selectPrimaryToken(walletList, primaryTokenId)

        val params = createTransactionRequestCreateParams(selectedToken)

        val formattedIds: MutableMap<TransactionRequestType, String> = mutableMapOf()

        IdlingResourceUtil.idlingResource.increment()
        GlobalScope.launch(Dispatchers.Main) {
            val result = GlobalScope.async(Dispatchers.IO) {
                val txReceiveResult = remoteRepository.createTransactionRequest(params)
                val txSendResult = remoteRepository.createTransactionRequest(
                    params.copy(type = TransactionRequestType.SEND, requireConfirmation = true)
                )
                return@async txReceiveResult to txSendResult
            }
            val (txReceive, txSend) = result.await()
            txReceive.either({ formattedIds[TransactionRequestType.RECEIVE] = it.data.formattedId }, this@PreloadResourceViewModel::handleAPIError)
            txSend.either({ formattedIds[TransactionRequestType.SEND] = it.data.formattedId }, this@PreloadResourceViewModel::handleAPIError)

            if (formattedIds.size == 2) {
                val message = "${formattedIds[TransactionRequestType.RECEIVE]}|${formattedIds[TransactionRequestType.SEND]}"
                logi(message)

                localRepository.saveTransactionRequestFormattedId(formattedIds)
                localRepository.saveTokenPrimary(selectedToken)

                /* Emit success event */
                liveTransactionRequestPrimaryTokenId.value = Event(selectedToken.id)
                IdlingResourceUtil.idlingResource.decrement()
            }
        }
    }

    // Select the token by priority:
    // 1. Select the token that matched with the `primaryTokenId`.
    // 2. Select the highest-amount token.
    // 3. Select the first token.
    fun selectPrimaryToken(walletList: WalletList, primaryTokenId: String?): Token {
        val theHighestBalance: (Balance) -> BigDecimal = { it.amount.divide(it.token.subunitToUnit) }
        return walletList.data[0].balances.find { it.token.id == primaryTokenId }?.token
            ?: walletList.data[0].balances.maxBy(theHighestBalance)?.token
            ?: walletList.data[0].balances[0].token
    }

    fun createTransactionRequestCreateParams(token: Token): TransactionRequestCreateParams {
        return TransactionRequestCreateParams(
            TransactionRequestType.RECEIVE,
            token.id,
            requireConfirmation = false
        )
    }

    fun handleAPIError(apiError: APIError) {
        liveCreateTransactionRequestFailed.value = Event(apiError)
        liveCloseButtonVisibility.value = View.VISIBLE
    }
}
