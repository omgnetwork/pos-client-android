package network.omisego.omgwallet.screen.auth.profile.transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.list.TransactionListParams
import network.omisego.omgwallet.R
import network.omisego.omgwallet.base.StateViewHolderBinding
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.databinding.ViewholderTransactionBinding
import network.omisego.omgwallet.extension.mutableLiveDataOf
import network.omisego.omgwallet.model.APIResult

class TransactionListViewModel(
    private val app: Application,
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val transformer: TransactionListTransformer
) : AndroidViewModel(app), StateViewHolderBinding<Transaction, ViewholderTransactionBinding> {
    val liveTransactionFailedDescription: MutableLiveData<String> by lazy { mutableLiveDataOf("") }
    val liveResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    val wallet: Wallet
        get() = localRepository.loadWallet()!!.data[0]

    /* helper function */
    val Transaction.isTopup: Boolean
        get() = this.from.accountId != null

    override fun bind(binding: ViewholderTransactionBinding, data: Transaction) {
        binding.transaction = data
        binding.viewModel = this
        binding.transformer = transformer
    }

    override fun resolvePayloadBundle(bundle: Bundle): Transaction? = null

    fun giveTransactionStatusDescription(transaction: Transaction) {
        transaction.error?.let {
            liveTransactionFailedDescription.value = transaction.error?.description
        }
        if (transaction.error == null) {
            liveTransactionFailedDescription.value = if (transaction.isTopup) {
                app.getString(
                    R.string.transaction_list_topup_info,
                    transaction.to.amount.divide(transaction.to.token.subunitToUnit),
                    transaction.to.token.symbol,
                    transaction.from.account?.name
                )
            } else {
                app.getString(
                    R.string.transaction_list_pay_info,
                    transaction.from.amount.divide(transaction.from.token.subunitToUnit),
                    transaction.from.token.symbol,
                    transaction.to.account?.name
                )
            }
        }
    }

    fun getTransaction(page: Int) {
        val params = TransactionListParams.create(
            page = page,
            perPage = 20,
            searchTerm = wallet.address
        )
        remoteRepository.getTransactions(params, liveResult)
    }
}
