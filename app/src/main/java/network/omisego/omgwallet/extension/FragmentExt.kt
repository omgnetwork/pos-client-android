package network.omisego.omgwallet.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

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
