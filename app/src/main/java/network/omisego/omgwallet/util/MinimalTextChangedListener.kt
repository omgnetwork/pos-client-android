package network.omisego.omgwallet.util

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.text.Editable
import android.text.TextWatcher

class MinimalTextChangeListener(private val afterTextChangedAction: (editable: Editable) -> Unit) : TextWatcher {
    override fun afterTextChanged(editable: Editable) = afterTextChangedAction.invoke(editable)
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}
