package network.omisego.omgwallet.pages

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.livedata.Event

class MainViewModel(
    val localRepository: LocalRepository
) : ViewModel() {
    val liveBalanceClickEvent: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }
    val liveProfileClickEvent: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }
    val liveQRClickEvent: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }

    fun handleBalanceClick(view: View) {
        liveBalanceClickEvent.value = Event(view)
    }

    fun handleProfileClick(view: View) {
        liveProfileClickEvent.value = Event(view)
    }

    fun handleQRClick(view: View) {
        liveQRClickEvent.value = Event(view)
    }

    fun loadWallets() = localRepository.loadWallet()
    fun hasAuthenticationToken() = localRepository.hasAuthenticationToken()
}
