package network.omisego.omgwallet.pages.signin

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.graphics.drawable.AnimatedVectorDrawable
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.ClientAuthenticationToken
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentSigninBinding
import network.omisego.omgwallet.extension.provideAndroidViewModel
import network.omisego.omgwallet.extension.runOnM
import network.omisego.omgwallet.extension.runOnMToP
import network.omisego.omgwallet.extension.runOnP
import network.omisego.omgwallet.extension.scrollBottom
import network.omisego.omgwallet.extension.toast

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var fingerprintViewModel: FingerprintBottomSheetViewModel
    private var scanFingerprintDialog: FingerprintBottomSheet? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signin,
            container,
            false
        )
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            scrollBottom()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideAndroidViewModel()
        fingerprintViewModel = provideAndroidViewModel()
        ivLogo.setImageDrawable(ContextCompat.getDrawable(ivLogo.context, R.drawable.omisego_logo_no_animated))

        setupDataBinding()
        ivLogo.setOnClickListener {
            runOnM {
                startLogoAnimate()
            }
        }
        btnSignIn.setOnClickListener { _ ->
            signIn()
        }
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                signIn()
                true
            }
            false
        }

        tvSignUp.movementMethod = LinkMovementMethod.getInstance()

        viewModel.liveSignupClick.observe(this, Observer {
            NavHostFragment.findNavController(this).navigate(R.id.action_signInFragment_to_signupFragment)
        })

        viewModel.liveToast.observe(this, Observer { it ->
            it?.let {
                context?.toast(it)
            }
        })

        runOnP { subscribeSignInWithFingerprintP() }
        runOnMToP { subscribeSignInWithFingerprintBelowP() }
    }

    private fun signIn() {
        viewModel.signIn()?.let { liveResult ->
            viewModel.showLoading(getString(R.string.sign_in_button_loading))
            liveResult.observe(this, Observer {
                viewModel.hideLoading(getString(R.string.sign_in_button))
                it?.handle(this::handleSignInSuccess, this::handleSignInError)
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun subscribeSignInWithFingerprintP() {
        viewModel.liveAuthenticationSucceeded.observe(this, Observer {
            if (viewModel.isFingerprintAvailable()) {
                etEmail.setText(viewModel.loadUserEmail())
                etPassword.setText(viewModel.loadUserPassword())
                signIn()
            } else {
                context?.toast(getString(R.string.dialog_fingerprint_option_not_enabled))
            }
        })

        viewModel.liveAuthenticationError.observe(this, Observer {
            if (it?.first == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT || it?.first == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT_PERMANENT) {
                context?.toast(getString(R.string.dialog_fingerprint_error_too_many_attempt))
            }
        })
    }

    private fun subscribeSignInWithFingerprintBelowP() {
        runOnM {
            viewModel.liveShowPre28FingerprintDialog.observe(this, Observer { it ->
                if (it == true) {
                    scanFingerprintDialog = FingerprintBottomSheet()
                    scanFingerprintDialog?.show(childFragmentManager, null)
                }
            })

            fingerprintViewModel.liveAuthPass.observe(this, Observer {
                if (!viewModel.isFingerprintAvailable()) {
                    context?.toast(getString(R.string.dialog_fingerprint_option_not_enabled))
                } else if (it == true) {
                    scanFingerprintDialog?.dismiss()
                    etEmail.setText(viewModel.loadUserEmail())
                    etPassword.setText(viewModel.loadUserPassword())
                    signIn()
                }
            })
        }
    }

    private fun proceed(data: ClientAuthenticationToken) {
        launch(UI) {
            viewModel.saveCredential(data).await()
            viewModel.saveUserEmail(etEmail.text.toString())
            findNavController().navigateUp()
        }
    }

    private fun handleSignInSuccess(data: ClientAuthenticationToken) {
        context?.toast(getString(R.string.sign_in_success, data.user.email))
        proceed(data)
    }

    private fun handleSignInError(error: APIError) {
        context?.toast(error.description)
    }

    private fun setupDataBinding() {
        binding.viewmodel = viewModel
        binding.emailValidator = viewModel.emailValidator
        binding.passwordValidator = viewModel.passwordValidator
        binding.setLifecycleOwner(this)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun startLogoAnimate() {
        val drawable = ivLogo.drawable
        if (drawable is AnimatedVectorDrawable) {
            drawable.start()
        }
    }
}
