package network.omisego.omgwallet.pages.splash

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.WalletList
import network.omisego.omgwallet.extension.subscribe
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.storage.Storage

class PreloadResourceRepository {
    fun loadWallets(liveAPIResult: MutableLiveData<APIResult>) {
        ClientProvider.client.getWallets().subscribe(liveAPIResult)
    }

    fun saveWallets(data: WalletList) {
        Storage.saveWallets(data)
    }
}
