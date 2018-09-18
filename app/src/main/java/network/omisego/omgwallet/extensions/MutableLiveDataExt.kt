package network.omisego.omgwallet.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 17/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.fetchedThenCache(fetch: (MutableLiveData<T>) -> LiveData<T>): LiveData<T> =
    if (value != null) {
        this
    } else {
        fetch(this)
    }
