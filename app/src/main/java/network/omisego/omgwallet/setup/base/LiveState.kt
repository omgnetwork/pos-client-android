package network.omisego.omgwallet.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData

open class LiveState<T>(initialState: T) : MutableLiveData<T>() {
    fun state(transform: (T) -> T) {
        value = transform(value!!)
    }

    init {
        value = initialState
    }
}
