package network.omisego.omgwallet.data.contract

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 22/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import network.omisego.omgwallet.livedata.Event
import network.omisego.omgwallet.model.APIResult

interface BalanceDataRepository {
    fun loadWallet(liveAPIResult: MutableLiveData<Event<APIResult>>, networkOnly: Boolean = false)
}
