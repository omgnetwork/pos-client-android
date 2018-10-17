package network.omisego.omgwallet.screen.auth

import androidx.lifecycle.ViewModel
import network.omisego.omgwallet.data.LocalRepository

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class AuthViewModel(
    private val localRepository: LocalRepository
) : ViewModel() {
    fun hasTransactionRequestFormattedId(): Boolean {
        return localRepository.hasFormattedId()
    }
}
