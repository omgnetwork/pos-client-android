package network.omisego.omgwallet.pages.balance

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.pages.balance.repository.BalanceLocalRepository
import network.omisego.omgwallet.pages.balance.repository.BalanceNetworkRepository

class BalanceRepository : BalanceDataRepository {
    private val localRepository: BalanceDataRepository by lazy { BalanceLocalRepository() }
    private val networkRepository: BalanceDataRepository by lazy { BalanceNetworkRepository() }

    override fun loadWallet(liveAPIResult: MutableLiveData<APIResult>, networkOnly: Boolean) {
        /* Show data from local first, then update. */
        localRepository.loadWallet(liveAPIResult, networkOnly)
        networkRepository.loadWallet(liveAPIResult, networkOnly)
    }
}

interface BalanceDataRepository {
    fun loadWallet(liveAPIResult: MutableLiveData<APIResult>, networkOnly: Boolean = false)
}
