package network.omisego.omgwallet.screen.auth.balance.detail

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.Balance
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.formatAmount
import network.omisego.omgwallet.extension.scaleAmount

class BalanceDetailItemViewModel(
    val app: Application
) : AndroidViewModel(app) {

    val liveTokenPrimaryText: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveTokenPrimaryEnabled: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun displayAmount(balance: Balance) = balance.scaleAmount().formatAmount()
    fun displayDate(balance: Balance) = app.getString(R.string.balance_detail_last_updated, balance.token.updatedAt)
    fun resolveTokenPrimaryText(balance: Balance, tokenPrimaryId: String?) {
        liveTokenPrimaryText.value = if (tokenPrimaryId == balance.token.id) {
            app.getString(R.string.balance_detail_token_primary)
        } else {
            app.getString(R.string.balance_detail_token_set_primary)
        }
    }

    fun resolveTokenPrimaryEnable(balance: Balance, tokenPrimaryId: String?) {
        liveTokenPrimaryEnabled.value = balance.token.id != tokenPrimaryId
    }
}
