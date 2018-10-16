package network.omisego.omgwallet.screen.auth.balance.detail

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import co.omisego.omisego.model.Balance
import network.omisego.omgwallet.R

class BalanceDetailItemViewModel(
    val app: Application
) : AndroidViewModel(app) {
    fun displayAmount(balance: Balance) = balance.displayAmount()
    fun balanceDate(balance: Balance) = app.getString(R.string.balance_detail_last_updated, balance.token.updatedAt)
}
