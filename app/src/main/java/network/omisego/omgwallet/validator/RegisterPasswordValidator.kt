package network.omisego.omgwallet.validator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import network.omisego.omgwallet.model.ValidateResult

class RegisterPasswordValidator(override var byPass: LiveData<Boolean>) : Validator(byPass) {
    private val lessThanEight: (String) -> Boolean = { it.length < 8 }
    private val missingLowerCase: (String) -> Boolean = { it -> !it.any { it in 'a'..'z' } }
    private val missingUpperCase: (String) -> Boolean = { it -> !it.any { it in 'A'..'Z' } }
    private val missingNumber: (String) -> Boolean = { it -> !it.any { it in '0'..'9' } }
    private val missingSpecialChar: (String) -> Boolean = { it -> !it.any { it in arrayOf('~', '!', '`', '@', '#', '+', '-') } }

    override fun check(text: String, updateUI: ((ValidateResult) -> Unit)?): ValidateResult {
        this.updateUI = updateUI
        this.recentText = text
        validation = when {
            byPass.value == false && lessThanEight(text) ->
                ValidateResult(false, "Password must contain at least 8 characters")
            byPass.value == false && missingLowerCase(text) ->
                ValidateResult(false, "Password must contain at least 1 lower case character")
            byPass.value == false && missingUpperCase(text) ->
                ValidateResult(false, "Password must contain at least 1 upper case character")
            byPass.value == false && missingNumber(text) ->
                ValidateResult(false, "Password must contain at least 1 numeric character")
            byPass.value == false && missingSpecialChar(text) ->
                ValidateResult(false, "Password must contain at least 1 special character")
            else -> ValidateResult(true)
        }
        updateUI?.invoke(validation)
        return validation
    }

    init {
        byPass.observe(this, Observer { check(this.recentText, this.updateUI) })
    }
}