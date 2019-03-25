package network.omisego.omgwallet.screen.auth.splash

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
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.WalletList
import kotlinx.android.synthetic.main.fragment_splash.*
import network.omisego.omgwallet.AppViewModel
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentSplashBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.findRootNavController
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.extension.provideAndroidViewModel
import network.omisego.omgwallet.extension.toast
import network.omisego.omgwallet.state.ErrorState
import network.omisego.omgwallet.util.EventObserver

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: PreloadResourceViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var args: SplashFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
        appViewModel = provideActivityViewModel()
        args = SplashFragmentArgs.fromBundle(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(R.layout.fragment_splash, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDataBinding()
        observeLiveData()

        btnClose.setOnClickListener {
            when (viewModel.errorState) {
                ErrorState.QUIT -> closeByQuit()
                ErrorState.SIGN_IN -> closeByBackToSignIn()
                ErrorState.BACK -> closeByBack()
            }
        }

        if (args.shouldLoadWallet) {
            viewModel.loadWallets()
        } else {
            viewModel.loadWalletLocally()
        }
    }

    private fun closeByQuit() {
        activity?.finishAndRemoveTask()
    }

    private fun closeByBack() {
        findNavController().navigateUp()
    }

    private fun closeByBackToSignIn() {
        appViewModel.onLoggedout()

        /* Clear all back-stack fragments */
        findRootNavController().popBackStack(R.id.authFragment, true)

        /* Go back to sign-in */
        findRootNavController().navigate(R.id.action_global_signInFragment)
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeLiveData() {
        viewModel.liveResult.observe(this, EventObserver {
            it.handle(this::handleLoadWalletSuccess, this::handleLoadWalletFail)
        })
        viewModel.liveTransactionRequestPrimaryTokenId.observe(this, EventObserver { primaryTokenId ->
            val balance = viewModel.loadBalances().find { it.token.id == primaryTokenId }
            context?.toast(viewModel.displayTokenPrimaryNotify(balance))
            findNavController().navigateUp()
        })
        viewModel.liveAPIError.observe(this, EventObserver {
            viewModel.setErrorState(it)
            tvCurrentStatus.text = it.description
        })
    }

    private fun handleLoadWalletSuccess(data: WalletList) {
        viewModel.runIfValidWalletList(data) {
            viewModel.saveWallet(data)
            viewModel.createTransactionRequest(data, args.primaryTokenId)
        }
    }

    private fun handleLoadWalletFail(error: APIError) {
        viewModel.handleAPIError(error)
    }
}
