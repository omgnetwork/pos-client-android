package network.omisego.omgwallet.screen.unauth.signup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.omisego.omisego.model.params.SignUpParams
import network.omisego.omgwallet.BuildConfig
import network.omisego.omgwallet.data.RemoteRepository
import network.omisego.omgwallet.extension.mutableLiveDataOf
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.validator.ConfirmPasswordValidator
import network.omisego.omgwallet.validator.EmailValidator
import network.omisego.omgwallet.validator.NotEmptyValidator
import network.omisego.omgwallet.validator.RegisterPasswordValidator
import network.omisego.omgwallet.validator.Validator

class SignupViewModel(
    val remoteRepository: RemoteRepository
) : ViewModel() {
    /* property extension */
    private val Array<Validator>.valid
        get() = !this.any { !it.validation.pass }

    val liveByPass: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(true) }
    val liveLoading: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(false) }

    /* Validator */
    val fullNameValidator: Validator by lazy { NotEmptyValidator(liveByPass) }
    val emailValidator: Validator by lazy { EmailValidator(liveByPass) }
    val passwordValidator: Validator by lazy { RegisterPasswordValidator(liveByPass) }
    val confirmPasswordValidator: ConfirmPasswordValidator by lazy { ConfirmPasswordValidator(liveByPass) }

    /* Control button signup enable */
    val livePassValidation: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(true) }

    val liveResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }

    fun checkValidationResult() {
        /* Check if pass all validation */
        livePassValidation.value = arrayOf(
            fullNameValidator,
            emailValidator,
            passwordValidator,
            confirmPasswordValidator
        ).valid
    }

    fun handleSignupClick() {

        /* Enable validator */
        liveByPass.value = false

        val signupParams = SignUpParams(
            emailValidator.recentText,
            passwordValidator.recentText,
            confirmPasswordValidator.recentText,
            BuildConfig.CLIENT_VERIFICATION_SIGNUP_PREFIX
        )

        liveLoading.value = true
        remoteRepository.signup(signupParams, liveResult)
    }
}