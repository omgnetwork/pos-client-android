package network.omisego.omgwallet.pages

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.ViewModel
import network.omisego.omgwallet.data.LocalRepository

class MainViewModel(
    val localRepository: LocalRepository
) : ViewModel() {
    fun loadWallets() = localRepository.loadWallet()
    fun hasAuthenticationToken() = localRepository.hasAuthenticationToken()
}
