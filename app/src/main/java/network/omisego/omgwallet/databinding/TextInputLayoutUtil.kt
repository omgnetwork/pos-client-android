package network.omisego.omgwallet.databinding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import network.omisego.omgwallet.model.ValidateResult
import network.omisego.omgwallet.util.MinimalTextChangeListener
import network.omisego.omgwallet.util.Validator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object TextInputLayoutUtil {
    @JvmStatic
    @BindingAdapter("validator")
    fun validate(view: TextInputLayout, validator: Validator) {
        val updateUI: (ValidateResult) -> Unit = {
            val (pass, reason) = it
            if (!pass) {
                view.error = reason
                view.isErrorEnabled = true
            } else {
                view.isErrorEnabled = false
            }
        }
        view.editText?.addTextChangedListener(MinimalTextChangeListener {
            validator.check(it.toString(), updateUI)
        })
    }
}