package network.omisego.omgwallet.pages.profile.main

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
import network.omisego.omgwallet.data.LocalRepository

class ProfileViewModel(
    private val app: Application,
    private val localRepository: LocalRepository
) : AndroidViewModel(app) {
    val liveAuthenticateSuccessful: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val liveTransaction: MutableLiveData<View> by lazy { MutableLiveData<View>() }
    val liveSignout: MutableLiveData<View> by lazy { MutableLiveData<View>() }

    fun clickSignout(view: View) {
        localRepository.clearSession()
        liveSignout.value = view
    }

    fun clickTransaction(view: View) {
        liveTransaction.value = view
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

    fun hasFingerprintSupport() = Goldfinger.Builder(app).build().hasFingerprintHardware()

    fun hasFingerprintPassword() = localRepository.hasFingerprintPassword()

    fun loadFingerprintOption() = localRepository.loadFingerprintOption()
}
