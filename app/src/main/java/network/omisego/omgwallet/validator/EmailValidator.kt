package network.omisego.omgwallet.validator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import network.omisego.omgwallet.R
import network.omisego.omgwallet.model.ValidateResult
import network.omisego.omgwallet.util.ContextUtil

class EmailValidator(override var byPass: LiveData<Boolean>) : Validator(byPass) {
    private val isInvalidEmailFormat: (String) -> Boolean = {
        !Patterns.EMAIL_ADDRESS.matcher(it).matches()
    }

    override fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult {
        this.updateUI = updateUI
        this.recentText = text
        validation = when {
            byPass.value == false && isInvalidEmailFormat(text) -> ValidateResult(false, ContextUtil.context.getString(R.string.validator_signin_email_invalid_format))
            else -> ValidateResult(true)
        }
        updateUI?.invoke(validation)
        return validation
    }

    init {
        byPass.observe(this, Observer { check(this.recentText, this.updateUI) })
    }
}
