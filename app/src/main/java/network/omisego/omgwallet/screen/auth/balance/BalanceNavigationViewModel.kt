package network.omisego.omgwallet.screen.auth.balance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class BalanceNavigationViewModel : ViewModel() {
    val liveNavigation: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    var lastPageSelected: Int = 0
}
