package network.omisego.omgwallet.screen.auth.balance

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.WalletList
import kotlinx.android.synthetic.main.fragment_balance.*
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.R
import network.omisego.omgwallet.base.BalanceDiffCallback
import network.omisego.omgwallet.base.LoadingRecyclerAdapter
import network.omisego.omgwallet.base.StateViewHolder
import network.omisego.omgwallet.base.UpdateAdapterDispatcher
import network.omisego.omgwallet.databinding.FragmentBalanceBinding
import network.omisego.omgwallet.databinding.ViewholderBalanceBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.extension.provideViewModel
import network.omisego.omgwallet.extension.toast
import network.omisego.omgwallet.livedata.EventObserver
import network.omisego.omgwallet.storage.Storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class BalanceFragment: Fragment(), UpdateAdapterDispatcher<Balance> {
    private var currentBalances: List<Balance> = listOf()
    private lateinit var binding: FragmentBalanceBinding
    private lateinit var viewModel: BalanceViewModel
    private lateinit var navigationViewModel: BalanceNavigationViewModel
    private lateinit var adapter: LoadingRecyclerAdapter<Balance, ViewholderBalanceBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
        navigationViewModel = provideActivityViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(R.layout.fragment_balance, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        binding.paddingRect = Rect(64, 0, 0, 0)
        viewModel.liveResult.observe(this, EventObserver {
            it.handle(this::handleLoadWalletSuccess, this::handleLoadWalletFail)
            if (swipeRefresh.isRefreshing) {
                swipeRefresh.isRefreshing = false
            }
        })

        viewModel.liveBalanceClickEvent.observe(this, EventObserver { balance ->
//            navigationViewModel.liveNavigation.value = R.layout.fragment_balance_detail
//            navigationViewModel.lastPageSelected = currentBalances.indexOfLast { it.token.id == balance.token.id }
            Navigation.findNavController(activity as MainActivity, R.id.content).navigate(R.id.action_balanceFragment_to_balanceDetailFragment)
        })

        viewModel.loadWallet()
    }

    override fun dispatchUpdate(oldList: List<Balance>, newList: List<Balance>, adapter: RecyclerView.Adapter<StateViewHolder>) {
        val diff = BalanceDiffCallback(oldList, newList)
        val result = DiffUtil.calculateDiff(diff)
        result.dispatchUpdatesTo(adapter)
    }

    private fun handleLoadWalletSuccess(walletList: WalletList) {
        currentBalances = walletList.data[0].balances
        adapter.reloadItems(currentBalances)
        viewModel.updateWallet(walletList)
    }

    private fun handleLoadWalletFail(error: APIError) {
        context?.toast(error.description)
        if (error.code == ErrorCode.USER_AUTH_TOKEN_NOT_FOUND) {
            Storage.clearSession()
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        adapter = LoadingRecyclerAdapter(R.layout.viewholder_balance_loading, R.layout.viewholder_balance, viewModel, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        swipeRefresh.setOnRefreshListener {
            viewModel.loadWallet()
        }
    }
}
