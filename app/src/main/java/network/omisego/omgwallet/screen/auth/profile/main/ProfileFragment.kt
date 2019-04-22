package network.omisego.omgwallet.screen.auth.profile.main

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_profile.*
import network.omisego.omgwallet.AppViewModel
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentProfileBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.findRootNavController
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.extension.provideAndroidViewModel
import network.omisego.omgwallet.extension.toast
import network.omisego.omgwallet.state.FingerprintDialogState
import network.omisego.omgwallet.util.EventObserver

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var appViewModel: AppViewModel
    private lateinit var viewModel: ProfileViewModel
    private lateinit var dialog: ConfirmFingerprintDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel = provideActivityViewModel()
        viewModel = provideAndroidViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(R.layout.fragment_profile, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.loadEmail()

        viewModel.liveTransaction.observe(this, EventObserver {
            it.let {
                findNavController().navigate(R.id.action_profileFragment_to_transactionListFragment)
            }
        })

        viewModel.liveSignout.observe(this, EventObserver {
            it.let {
                appViewModel.onLoggedout()

                /* Clear all back-stack fragments */
                findRootNavController().popBackStack(R.id.authFragment, true)

                /* Go back to sign-in */
                findRootNavController().navigate(R.id.action_global_signInFragment)
            }
        })

        viewModel.liveFingerprintDialogState.observe(this, Observer {
            when (it) {
                FingerprintDialogState.STATE_WRONG_PASSWORD -> {
                }
                FingerprintDialogState.STATE_CANCELED -> {
                    viewModel.handleFingerprintOption(false)
                    switchFingerprint.isChecked = false
                }
                FingerprintDialogState.STATE_ENABLED -> {
                    dialog.dismiss()
                    context?.toast(getString(R.string.setting_help_enable_finger_print_successfully))
                    switchFingerprint.isChecked = true
                }
            }
        })

        switchFingerprint.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !viewModel.hasFingerprintPassword()) {
                dialog = ConfirmFingerprintDialog().apply {
                    liveFingerprintDialogState = viewModel.liveFingerprintDialogState
                }
                dialog.show(childFragmentManager, null)
            } else if (!isChecked) {
                viewModel.handleFingerprintOption(false)
            }
        }
    }
}
