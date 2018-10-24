package network.omisego.omgwallet.pages.profile.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.ClientAuthenticationToken
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_enter_password.*
import kotlinx.android.synthetic.main.bottomsheet_enter_password.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.provideViewModel
import network.omisego.omgwallet.extension.toast
import network.omisego.omgwallet.state.FingerprintDialogState

class ConfirmFingerprintDialog : BottomSheetDialogFragment() {
    private lateinit var viewModel: ConfirmFingerprintViewModel
    var liveFingerprintDialogState: MutableLiveData<FingerprintDialogState>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        liveFingerprintDialogState?.value = FingerprintDialogState.STATE_CANCELED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.bottomsheet_enter_password, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btnConfirm.setOnClickListener {
            send()
        }

        view.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                send()
            }
            false
        }

        viewModel.liveAPIResult.observe(this, Observer {
            it?.handle(this::handleSignInSuccess, this::handleSignInError)
        })
    }

    private fun handleSignInSuccess(data: ClientAuthenticationToken) {
        launch(UI) {
            viewModel.saveCredential(data).await()
            viewModel.saveUserPassword(etPassword.text.toString())
            liveFingerprintDialogState?.value = FingerprintDialogState.STATE_ENABLED
            btnConfirm.isEnabled = true
        }
    }

    private fun handleSignInError(error: APIError) {
        context?.toast(error.description)
        liveFingerprintDialogState?.value = FingerprintDialogState.STATE_WRONG_PASSWORD
        btnConfirm.isEnabled = true
    }

    private fun send() {
        btnConfirm.isEnabled = false
        viewModel.signIn(etPassword.text.toString())
    }
}
