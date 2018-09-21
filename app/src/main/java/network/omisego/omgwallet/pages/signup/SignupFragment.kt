package network.omisego.omgwallet.pages.signup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Empty
import kotlinx.android.synthetic.main.fragment_signup.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentSignupBinding
import network.omisego.omgwallet.extension.getDrawableCompat
import network.omisego.omgwallet.extension.provideViewModel
import network.omisego.omgwallet.extension.toast
import network.omisego.omgwallet.listener.MinimalTextChangeListener

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupDataBinding()
        viewModel.liveResult.observe(this, Observer {
            viewModel.liveLoading.value = false
            it.handle(this::handleSignupSuccess, this::handleSignupFail)
        })
    }

    private fun setupDataBinding() {
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.etPassword.addTextChangedListener(MinimalTextChangeListener {
            viewModel.confirmPasswordValidator.currentPassword = it.toString()
        })
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
        toolbar.navigationIcon = context?.getDrawableCompat(R.drawable.ic_arrow_back)
        toolbar.title = getString(R.string.sign_up_title)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun handleSignupSuccess(data: Empty) {
        context?.toast("Register successfully. Please confirm your email.")
        findNavController().navigate(R.id.action_signupFragment_to_confirmFragment)
    }

    private fun handleSignupFail(error: APIError) {
        context?.toast(error.description)
    }

    private fun scrollToBottom() {
        nestedScrollView.scrollTo(0, nestedScrollView.bottom)
    }
}
