package network.omisego.omgwallet.screen.auth.profile.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 25/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import co.infinum.goldfinger.Goldfinger
import network.omisego.omgwallet.BuildConfig
import network.omisego.omgwallet.data.LocalRepository
import network.omisego.omgwallet.livedata.Event
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.state.FingerprintDialogState

class ProfileViewModel(
    private val app: Application,
    private val localRepository: LocalRepository
) : AndroidViewModel(app) {
    val liveFingerprintDialogState: MutableLiveData<FingerprintDialogState> by lazy { MutableLiveData<FingerprintDialogState>() }
    val liveTransaction: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }
    val liveSignout: MutableLiveData<Event<View>> by lazy { MutableLiveData<Event<View>>() }
    val liveVersionName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveEndpoint: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun clickSignout(view: View) {
        liveSignout.value = Event(view)
    }

    fun clickTransaction(view: View) {
        liveTransaction.value = Event(view)
    }

    fun deleteFingerprintCredential() {
        localRepository.deleteFingerprintCredential()
    }

    fun handleFingerprintOption(checked: Boolean) {
        localRepository.saveFingerprintOption(checked)
        if (!checked) {
            deleteFingerprintCredential()
        }
    }

    init {
        liveVersionName.value = BuildConfig.VERSION_NAME
        liveEndpoint.value = ClientProvider.clientSetup.baseURL
    }

    fun hasFingerprintSupport() = Goldfinger.Builder(app).build().hasFingerprintHardware()

    fun hasFingerprintPassword() = localRepository.hasFingerprintPassword()

    fun loadFingerprintOption() = localRepository.loadFingerprintOption()
}
