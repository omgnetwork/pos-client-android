package network.omisego.omgwallet.extension

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun Context.dpToPx(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
}

fun Context.getDrawableCompat(@DrawableRes res: Int) = ContextCompat.getDrawable(this, res)

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toaster.toast?.cancel()
    Toaster.toast = Toast.makeText(this, msg, duration)
    Toaster.toast?.show()
}

object Toaster {
    var toast: Toast? = null
}
