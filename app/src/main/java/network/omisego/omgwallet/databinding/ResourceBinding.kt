package network.omisego.omgmerchant.databinding

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object ResourceBinding {
    @JvmStatic
    @BindingAdapter("backgroundDrawable")
    fun setBackgroundDrawable(
        view: View,
        @DrawableRes drawableResId: Int
    ) {
        view.background = ContextCompat.getDrawable(view.context, drawableResId)
    }
}