package network.omisego.omgwallet.screen.auth.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ProfileNavigationViewModel : ViewModel() {
    val liveNavigation: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
}
