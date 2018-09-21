package network.omisego.omgwallet.pages

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.ViewModel

class MainViewModel(
    val repository: MainRepository
) : ViewModel() {
    fun loadWallets() = repository.loadWallets()
    fun loadUserEmail() = repository.loadUserEmail()
}
