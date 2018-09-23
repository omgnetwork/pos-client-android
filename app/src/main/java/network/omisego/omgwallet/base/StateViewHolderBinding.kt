package network.omisego.omgwallet.base

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.databinding.ViewDataBinding

interface StateViewHolderBinding<T, R : ViewDataBinding> {
    fun bind(binding: R, data: T)
}
