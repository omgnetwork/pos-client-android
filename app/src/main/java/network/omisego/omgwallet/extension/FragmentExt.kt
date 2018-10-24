package network.omisego.omgwallet.extension

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.R

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

fun Fragment.findRootNavController()= Navigation.findNavController(activity as MainActivity, R.id.nav_host)