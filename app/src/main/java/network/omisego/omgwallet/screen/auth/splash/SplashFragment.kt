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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.WalletList
import kotlinx.android.synthetic.main.fragment_splash.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentSplashBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.logi
import network.omisego.omgwallet.extension.provideAndroidViewModel
import network.omisego.omgwallet.extension.toast
import network.omisego.omgwallet.livedata.EventObserver

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: PreloadResourceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(R.layout.fragment_splash, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDataBinding()
        observeLiveData()
        viewModel.loadWallets()
        btnClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
    }

    private fun observeLiveData() {
        viewModel.liveResult.observe(this, EventObserver {
            it.handle(this::handleLoadWalletSuccess, this::handleLoadWalletFail)
        })
        viewModel.liveTransactionRequestFormattedId.observe(this, EventObserver {
            logi("TransactionRequestFormattedId: $it")
            findNavController().navigateUp()
        })
        viewModel.liveCreateTransactionRequestFailed.observe(this, EventObserver {
            context?.toast(it.description, Toast.LENGTH_LONG)
            tvCurrentStatus.text = it.description
        })
    }

    private fun handleLoadWalletSuccess(data: WalletList) {
        viewModel.saveWallet(data)
        viewModel.createTransactionRequest(data)
    }

    private fun handleLoadWalletFail(error: APIError) {
        viewModel.handleAPIError(error)
    }
}
