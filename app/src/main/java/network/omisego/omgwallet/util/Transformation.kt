package network.omisego.omgwallet.util

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations

fun <T, R> LiveData<T>.map(transform: (T) -> R) = Transformations.map(this, transform)

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

fun <T, R> LiveData<T>.switchMap(transform: (T) -> LiveData<R>) = Transformations.switchMap(this, transform)
