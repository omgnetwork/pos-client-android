package network.omisego.omgwallet.pages.balance.repository

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import network.omisego.omgwallet.extension.subscribe
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.pages.balance.BalanceDataRepository

class BalanceNetworkRepository : BalanceDataRepository {
    override fun loadWallet(liveAPIResult: MutableLiveData<APIResult>, networkOnly: Boolean) {
        ClientProvider.client.getWallets().subscribe(liveAPIResult)
    }
}
