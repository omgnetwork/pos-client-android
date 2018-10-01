package network.omisego.omgwallet.pages.profile.transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import co.omisego.omisego.model.transaction.list.TransactionListParams
import network.omisego.omgwallet.extension.subscribe
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.storage.Storage

class TransactionListRepository {
    fun getTransactions(params: TransactionListParams): LiveData<APIResult> {
        return ClientProvider.client
            .getTransactions(params)
            .subscribe()
    }

    fun getAccount() = Storage.loadUser()

    fun getWallet() = Storage.loadWallets()
}