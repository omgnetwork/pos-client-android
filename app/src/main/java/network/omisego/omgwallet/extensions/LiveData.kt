package network.omisego.omgwallet.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData

fun <T> mutableLiveDataOf(initial: T? = null): MutableLiveData<T> {
    return MutableLiveData<T>().apply { this.value = initial }
}
