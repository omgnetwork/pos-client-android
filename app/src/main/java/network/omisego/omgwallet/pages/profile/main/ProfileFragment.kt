package network.omisego.omgwallet.pages.profile.main

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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_profile.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentProfileBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.extension.provideAndroidViewModel
import network.omisego.omgwallet.extension.toast
import network.omisego.omgwallet.pages.profile.ProfileNavigationViewModel
import network.omisego.omgwallet.state.FingerprintDialogState

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var navigationViewModel: ProfileNavigationViewModel
    private lateinit var dialog: ConfirmFingerprintDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
        navigationViewModel = provideActivityViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(R.layout.fragment_profile, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.liveTransaction.observe(this, Observer {
            it.getContentIfNotHandled().let { _ ->
                navigationViewModel.liveNavigation.value = R.layout.fragment_transaction
            }
        })

        viewModel.liveSignout.observe(this, Observer {
            it.getContentIfNotHandled().let { _ ->
                findNavController().navigate(R.id.action_main_to_signInFragment)
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
                    viewModel.handleFingerprintOption(true)
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
                viewModel.deleteFingerprintCredential()
            }
        }

        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
        hostActivity.supportActionBar?.title = getString(R.string.profile_title)
    }
}
