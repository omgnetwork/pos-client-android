package network.omisego.omgwallet.screen.auth.confirm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.TransactionConsumption
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.formatAmount
import network.omisego.omgwallet.extension.scaleAmount
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.repository.LocalRepository
import network.omisego.omgwallet.repository.RemoteRepository
import network.omisego.omgwallet.util.Event

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ConfirmTransactionRequestViewModel(
    val app: Application,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : AndroidViewModel(app) {
    private var txConsumptionApprove: TransactionConsumption? = null
    val liveAmountText: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveSendToText: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveErrorDescription: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveResult: MutableLiveData<Event<APIResult>> by lazy { MutableLiveData<Event<APIResult>>() }

    fun formatAmount(txConsumption: TransactionConsumption) {
        liveAmountText.value = app.getString(
            R.string.confirm_transaction_request_amount,
            txConsumption.scaleAmount().formatAmount(),
            txConsumption.transactionRequest.token.symbol
        )
    }

    fun formatSendTo(txConsumption: TransactionConsumption) {
        liveSendToText.value = txConsumption.account?.name ?: txConsumption.user?.email
    }

    fun formatApproveError(error: APIError) {
        liveErrorDescription.value = if (error.code == ErrorCode.TRANSACTION_INSUFFICIENT_FUNDS) {
            val tokenId = txConsumptionApprove?.token?.id
            val balances = localRepository.loadWallets()?.data?.get(0)?.balances
            app.getString(
                R.string.confirm_transaction_request_error_not_has_enough_fund,
                balances?.findLast { it.token.id == tokenId }?.displayAmount(2),
                txConsumptionApprove?.token?.symbol
            )
        } else {
            error.description
        }
    }

    fun handleApproveClick(txConsumption: TransactionConsumption) {
        txConsumptionApprove = txConsumption
        remoteRepository.approveTransaction(txConsumption.id, liveResult)
    }

    fun handleRejectClick(txConsumption: TransactionConsumption) {
        remoteRepository.rejectTransaction(txConsumption.id, liveResult)
    }
}
