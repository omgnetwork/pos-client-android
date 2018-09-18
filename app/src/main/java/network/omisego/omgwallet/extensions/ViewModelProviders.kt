package network.omisego.omgwallet.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import network.omisego.omgwallet.AndroidViewModelFactory
import network.omisego.omgwallet.ViewModelFactory

inline fun <reified T : ViewModel> Fragment.provideActivityViewModel(): T {
    return ViewModelProviders.of(activity!!, ViewModelFactory())[T::class.java]
}

inline fun <reified T : AndroidViewModel> Fragment.provideActivityAndroidViewModel(): T {
    return ViewModelProviders.of(activity!!, ViewModelProvider.AndroidViewModelFactory(this.activity!!.application))[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.provideViewModel(): T {
    return ViewModelProviders.of(this, ViewModelFactory())[T::class.java]
}

inline fun <reified T : AndroidViewModel> Fragment.provideAndroidViewModel(): T {
    return ViewModelProviders.of(this, AndroidViewModelFactory(this.activity!!.application))[T::class.java]
}

inline fun <reified T : ViewModel> AppCompatActivity.provideViewModel(): T {
    return ViewModelProviders.of(this, ViewModelFactory())[T::class.java]
}

inline fun <reified T : AndroidViewModel> AppCompatActivity.provideAndroidViewModel(): T {
    return ViewModelProviders.of(this, AndroidViewModelFactory(application))[T::class.java]
}

