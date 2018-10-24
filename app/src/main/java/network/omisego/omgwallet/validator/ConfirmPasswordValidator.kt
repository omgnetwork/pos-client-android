package network.omisego.omgwallet.validator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import network.omisego.omgwallet.R
import network.omisego.omgwallet.model.ValidateResult
import network.omisego.omgwallet.util.ContextUtil

class ConfirmPasswordValidator(override var byPass: LiveData<Boolean>) : Validator(byPass) {
    var currentPassword: String = ""
        set(value) {
            field = value
            check(this.recentText, this.updateUI)
        }

    override fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult {
        this.updateUI = updateUI
        this.recentText = text
        validation = when {
            byPass.value == false && text != currentPassword ->
                ValidateResult(false, ContextUtil.context.getString(R.string.validator_signup_confirm_password_not_matched))
            else -> ValidateResult(true)
        }
        updateUI?.invoke(validation)
        return validation
    }

    init {
        byPass.observe(this, Observer { check(this.recentText, this.updateUI) })
    }
}