package network.omisego.omgwallet.screen.auth.balance.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.Token
import network.omisego.omgwallet.R
import network.omisego.omgwallet.data.LocalRepository

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class BalanceDetailViewModel(
    val app: Application,
    val localRepository: LocalRepository
) : AndroidViewModel(app) {
    val liveTokenPrimaryId: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun displayTokenPrimaryNotify(balance: Balance) = app.getString(R.string.balance_detail_primary_token_set_notify, balance.token.symbol)

    fun loadBalances() = localRepository.loadWallet()?.data?.get(0)?.balances!!

    fun saveTokenPrimary(token: Token) {
        liveTokenPrimaryId.value = token.id
        localRepository.saveTokenPrimary(token)
    }

    fun loadTokenPrimary(): String? {
        return localRepository.loadTokenPrimary()
    }
}
