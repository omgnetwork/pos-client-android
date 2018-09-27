package network.omisego.omgwallet.pages.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.Application
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.omisego.omisego.model.ClientAuthenticationToken
import co.omisego.omisego.model.params.LoginParams
import kotlinx.coroutines.experimental.Deferred
import network.omisego.omgwallet.R
import network.omisego.omgwallet.base.LiveState
import network.omisego.omgwallet.extension.mapPropChanged
import network.omisego.omgwallet.extension.mutableLiveDataOf
import network.omisego.omgwallet.extension.runBelowM
import network.omisego.omgwallet.extension.runOnMToP
import network.omisego.omgwallet.extension.runOnP
import network.omisego.omgwallet.livedata.SingleLiveEvent
import network.omisego.omgwallet.model.APIResult
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.util.BiometricUtil
import network.omisego.omgwallet.util.ContextUtil.context
import network.omisego.omgwallet.util.click
import network.omisego.omgwallet.util.spannable
import network.omisego.omgwallet.validator.EmailValidator
import network.omisego.omgwallet.validator.PasswordValidator
import network.omisego.omgwallet.validator.Validator

class SignInViewModel(
    private val app: Application,
    private val signInRepository: SignInRepository,
    private val biometricUtil: BiometricUtil
) : AndroidViewModel(app) {
    private val liveState: LiveState<SignInState> by lazy {
        LiveState(SignInState("", "", context.getString(R.string.sign_in_button), false))
    }
    private val liveByPassValidation: MutableLiveData<Boolean> by lazy { mutableLiveDataOf(true) }
    val liveAPIResult: MutableLiveData<APIResult> by lazy { MutableLiveData<APIResult>() }
    val liveBtnText: LiveData<String> by lazy { liveState.mapPropChanged { it.btnText } }
    val liveLoading: LiveData<Boolean> by lazy { liveState.mapPropChanged { it.loading } }
    val liveToast: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val liveSignupClick: SingleLiveEvent<View> by lazy { SingleLiveEvent<View>() }

    private val signupClickableSpan: ClickableSpan by lazy {
        object : ClickableSpan() {
            override fun onClick(view: View) {
                liveSignupClick.value = view
            }
        }
    }

    val emailValidator: Validator by lazy { EmailValidator(liveByPassValidation) }
    val passwordValidator: Validator by lazy { PasswordValidator(liveByPassValidation) }

    /* Biometric LiveData */
    val liveAuthenticationError: MutableLiveData<Pair<Int, CharSequence?>> by lazy { MutableLiveData<Pair<Int, CharSequence?>>() }
    val liveAuthenticationSucceeded: MutableLiveData<BiometricPrompt.CryptoObject> by lazy { MutableLiveData<BiometricPrompt.CryptoObject>() }
    val liveAuthenticationFailed: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    val liveAuthenticationHelp: MutableLiveData<Pair<Int, CharSequence?>> by lazy { MutableLiveData<Pair<Int, CharSequence?>>() }

    /* OnClick LiveData */
    val liveShowPre28FingerprintDialog: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    /* Text */
    val signUpText: SpannableString by lazy {
        val spannableString = spannable {
            click(app.getString(R.string.sign_in_sign_up), signupClickableSpan)
        }
        SpannableString(TextUtils.concat(app.getString(R.string.sign_in_dont_have_account), " ", spannableString))
    }

    private lateinit var biometricCallback: BiometricCallback

    private var prompt: BiometricPrompt? = null

    fun handleFingerprintClick() {
        /*
       * The BiometricPrompt is now only supported android P or above.
       * BiometricPrompt compatible version will be coming with androidX package next release:
       * https://android.googlesource.com/platform/frameworks/support/+/androidx-master-dev/biometric/src/main/java/androidx/biometrics/BiometricPrompt.java
       */
        runOnP {
            biometricCallback = BiometricCallback(
                liveAuthenticationError,
                liveAuthenticationSucceeded,
                liveAuthenticationFailed,
                liveAuthenticationHelp
            )

            prompt = BiometricPrompt.Builder(app)
                .setTitle(app.getString(R.string.dialog_fingerprint_title))
                .setSubtitle(app.getString(R.string.dialog_fingerprint_subtitle))
                .setDescription(app.getString(R.string.dialog_fingerprint_description))
                .setNegativeButton(
                    app.getString(R.string.dialog_fingerprint_btn_cancel),
                    app.mainExecutor,
                    DialogInterface.OnClickListener { dialog, _ -> dialog?.dismiss() }
                )
                .build()

            prompt?.authenticate(
                biometricUtil.createCancellationSignal(),
                app.mainExecutor,
                biometricCallback
            )
        }

        runOnMToP {
            liveShowPre28FingerprintDialog.value = true
        }

        runBelowM {
            liveToast.value = app.getString(R.string.dialog_fingerprint_unsupported)
        }
    }

    fun isFingerprintAvailable() = signInRepository.loadFingerprintOption()

    fun loadUserEmail(): String = signInRepository.loadUserEmail()

    fun loadUserPassword(): String = signInRepository.loadFingerprintCredential()

    fun signIn(): LiveData<APIResult>? {
        val (email, password) = liveState.value ?: return null
        liveByPassValidation.value = false
        arrayOf(emailValidator, passwordValidator).find { !it.validation.pass }?.let { return null }
        showLoading(app.getString(R.string.sign_in_button_loading))
        return signInRepository.signIn(LoginParams(email, password), liveAPIResult)
    }

    fun saveCredential(data: ClientAuthenticationToken): Deferred<Unit> {
        signInRepository.saveUser(data.user)
        return signInRepository.saveCredential(
            Credential(
                data.authenticationToken
            )
        )
    }

    fun saveUserEmail(email: String) {
        signInRepository.saveUserEmail(email)
    }

    fun showLoading(text: String) {
        liveState.state { it.copy(loading = true, btnText = text) }
    }

    fun hideLoading(text: String) {
        liveState.state { it.copy(loading = false, btnText = text) }
    }

    fun updateEmail(text: CharSequence) {
        liveState.state { it.copy(email = text.toString()) }
    }

    fun updatePassword(text: CharSequence) {
        liveState.state { it.copy(password = text.toString()) }
    }

    override fun onCleared() {
        super.onCleared()
        emailValidator.onCleared()
        passwordValidator.onCleared()
    }

    init {
        liveState.state { it.copy(loading = false) }
    }
}
