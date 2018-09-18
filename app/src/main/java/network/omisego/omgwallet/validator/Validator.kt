package network.omisego.omgwallet.validator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import network.omisego.omgwallet.model.ValidateResult

abstract class Validator(
    open var byPass: LiveData<Boolean>
) : LifecycleOwner {
    private val lifecycleOwnerRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
    var recentText: String = ""
    var validation: ValidateResult = ValidateResult(true)
    var updateUI: ((ValidateResult) -> Unit)? = null

    abstract fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult

    fun onCleared() {
        lifecycleOwnerRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun getLifecycle(): Lifecycle = lifecycleOwnerRegistry

    init {
        lifecycleOwnerRegistry.markState(Lifecycle.State.STARTED)
    }
}
