package network.omisego.omgwallet.pages.balance

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.WalletList
import network.omisego.omgwallet.base.StateViewHolderBinding
import network.omisego.omgwallet.databinding.ViewholderBalanceBinding
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.storage.Storage

class BalanceViewModel(
    private val repository: BalanceDataRepository
) : ViewModel(), StateViewHolderBinding<Balance, ViewholderBalanceBinding> {

    override fun bind(binding: ViewholderBalanceBinding, data: Balance) {
        binding.balance = data
        binding.displayAmount = data.displayAmount(2)
    }

    val liveResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    fun loadWallet(networkOnly: Boolean = false) {
        repository.loadWallet(liveResult, networkOnly)
    }

    fun updateWallet(walletList: WalletList) {
        Storage.saveWallets(walletList)
    }
}