package network.omisego.omgwallet.pages.splash

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.omisego.omisego.model.WalletList
import network.omisego.omgwallet.model.APIResult

class PreloadResourceViewModel(
    private val repository: PreloadResourceRepository
) : ViewModel() {
    val liveResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    fun loadWallets() {
        repository.loadWallets(liveResult)
    }

    fun saveWallet(data: WalletList) {
        repository.saveWallets(data)
    }
}
