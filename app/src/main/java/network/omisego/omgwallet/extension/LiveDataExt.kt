package network.omisego.omgwallet.extension

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <T> mutableLiveDataOf(initial: T? = null): MutableLiveData<T> {
    return MutableLiveData<T>().apply { this.value = initial }
}

/**
 * Don't dispatch if the transformed data is not changed.
 */
fun <T, R> LiveData<T>.mapPropChanged(transform: (T) -> R): LiveData<R> {
    return MediatorLiveData<R>().apply {
        addSource(this@mapPropChanged) {
            if (it == null) return@addSource
            val newProp = transform.invoke(it)
            if (newProp != value) {
                value = newProp
            }
        }
    }
}
