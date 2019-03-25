package network.omisego.omgwallet.screen.auth

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.ViewModel
import network.omisego.omgwallet.GraphMainDirections
import network.omisego.omgwallet.repository.LocalRepository
import network.omisego.omgwallet.repository.RemoteRepository

class MainViewModel(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    fun hasTransactionRequestFormattedId(): Boolean {
        return localRepository.hasFormattedId()
    }

    fun loadPrimaryTokenId(): String? {
        return localRepository.loadTokenPrimary()
    }

    fun provideSplashDirection() = GraphMainDirections.actionGlobalSplash(loadPrimaryTokenId())
}
