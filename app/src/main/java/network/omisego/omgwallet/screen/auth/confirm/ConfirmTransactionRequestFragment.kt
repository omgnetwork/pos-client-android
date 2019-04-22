package network.omisego.omgwallet.screen.auth.confirm

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.TransactionConsumption
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentConfirmTransactionRequestBinding
import network.omisego.omgwallet.extension.provideAndroidViewModel
import network.omisego.omgwallet.util.EventObserver

class ConfirmTransactionRequestFragment : Fragment() {
    private lateinit var binding: FragmentConfirmTransactionRequestBinding
    private lateinit var viewModel: ConfirmTransactionRequestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_transaction_request, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txConsumption = ConfirmTransactionRequestFragmentArgs.fromBundle(arguments!!).transactionConsumption
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.txConsumption = txConsumption

        viewModel.formatAmount(txConsumption)
        viewModel.formatSendTo(txConsumption)
        viewModel.liveResult.observe(this, EventObserver { result ->
            result.handle<TransactionConsumption>({}, this::handleApproveResult)
        })
    }

    private fun handleApproveResult(error: APIError) {
        viewModel.formatApproveError(error)
    }
}
