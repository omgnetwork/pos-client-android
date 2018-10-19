package network.omisego.omgwallet.extension

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(msg: String, duration: Int = Snackbar.LENGTH_LONG): Snackbar {
    val snackbar = Snackbar.make(this, msg, duration)
    val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.maxLines = 5
    return snackbar
}
