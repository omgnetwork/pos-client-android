package network.omisego.omgwallet.extension

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun <T : ViewDataBinding> Fragment.bindingInflate(
    @LayoutRes layoutRes: Int,
    container: ViewGroup?
) = DataBindingUtil.inflate<T>(
    this.layoutInflater,
    layoutRes,
    container,
    false
)

fun Fragment.replaceFragment(
    @IdRes layoutContainerRes: Int,
    fragment: Fragment
) {
    childFragmentManager
        .beginTransaction()
        .replace(layoutContainerRes, fragment)
        .commit()
}

fun Fragment.replaceFragmentBackstack(
    @IdRes layoutContainerRes: Int,
    fragment: Fragment
) {
    childFragmentManager
        .beginTransaction()
        .addToBackStack(null)
        .replace(layoutContainerRes, fragment)
        .commit()
}
