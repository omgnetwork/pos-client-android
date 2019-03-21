package network.omisego.omgwallet.screen.auth.balance.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.Token
import network.omisego.omgwallet.GraphMainDirections
import network.omisego.omgwallet.repository.LocalRepository
import network.omisego.omgwallet.util.Event

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class BalanceDetailViewModel(
    val app: Application,
    val localRepository: LocalRepository
) : AndroidViewModel(app) {
    val liveTokenPrimaryId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveEventTokenPrimaryId: MutableLiveData<Event<String>> by lazy { MutableLiveData<Event<String>>() }

    fun loadBalances() = localRepository.loadWallet()?.data?.get(0)?.balances!!

    fun saveTokenPrimary(token: Token) {
        liveTokenPrimaryId.value = token.id
        liveEventTokenPrimaryId.value = Event(token.id)
    }

    fun provideSplashDirection(primaryTokenId: String) = GraphMainDirections
        .actionGlobalSplash()
        .setPrimaryTokenId(primaryTokenId)
        .setShouldLoadWallet(false)

    fun loadTokenPrimary(): String? {
        return localRepository.loadTokenPrimary()
    }
}
