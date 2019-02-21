package network.omisego.omgwallet.databinding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import network.omisego.omgwallet.listener.MinimalTextChangeListener
import network.omisego.omgwallet.model.ValidateResult
import network.omisego.omgwallet.screen.unauth.signup.SignupViewModel
import network.omisego.omgwallet.validator.Validator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object TextInputLayoutBinding {
    @JvmStatic
    @BindingAdapter("validator", "verifier")
    fun validate(view: TextInputLayout, validator: Validator, verifier: SignupViewModel.Verifier) {
        val uiFeedback = addUIFeedback(view, validator)
        view.editText?.addTextChangedListener(MinimalTextChangeListener {
            validator.check(it.toString(), uiFeedback)
            verifier.checkValidationResult()
        })
    }

    @JvmStatic
    @BindingAdapter("validator")
    fun validate(view: TextInputLayout, validator: Validator) {
        val uiFeedback = addUIFeedback(view, validator)
        view.editText?.addTextChangedListener(MinimalTextChangeListener {
            validator.check(it.toString(), uiFeedback)
        })
    }

    private fun addUIFeedback(view: TextInputLayout, validator: Validator): (ValidateResult) -> Unit {
        val uiFeedback: (ValidateResult) -> Unit = {
            val (pass, reason) = it
            if (!pass) {
                view.error = reason
                view.isErrorEnabled = true
            } else {
                view.isErrorEnabled = false
            }
        }
        validator.byPass.observeForever { validator.check(view.editText!!.text.toString(), uiFeedback) }
        return uiFeedback
    }
}