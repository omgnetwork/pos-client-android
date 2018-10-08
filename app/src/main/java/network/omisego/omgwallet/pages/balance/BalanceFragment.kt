package network.omisego.omgwallet.pages.balance

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.WalletList
import kotlinx.android.synthetic.main.fragment_balance.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.base.BalanceDiffCallback
import network.omisego.omgwallet.base.LoadingRecyclerAdapter
import network.omisego.omgwallet.base.StateViewHolder
import network.omisego.omgwallet.base.UpdateAdapterDispatcher
import network.omisego.omgwallet.databinding.FragmentBalanceBinding
import network.omisego.omgwallet.databinding.ViewholderBalanceBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.provideViewModel
import network.omisego.omgwallet.extension.toast
import network.omisego.omgwallet.storage.Storage

class BalanceFragment : Fragment(), UpdateAdapterDispatcher<Balance> {
    private lateinit var binding: FragmentBalanceBinding
    private lateinit var viewModel: BalanceViewModel
    private lateinit var adapter: LoadingRecyclerAdapter<Balance, ViewholderBalanceBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(R.layout.fragment_balance, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()

        binding.paddingRect = Rect(64, 0, 0, 0)
        viewModel.liveResult.observe(this, Observer {
            it.handle(this::handleLoadWalletSuccess, this::handleLoadWalletFail)
            if (swipeRefresh.isRefreshing) {
                swipeRefresh.isRefreshing = false
            }
        })
        viewModel.loadWallet()
    }

    override fun dispatchUpdate(oldList: List<Balance>, newList: List<Balance>, adapter: RecyclerView.Adapter<StateViewHolder>) {
        val diff = BalanceDiffCallback(oldList, newList)
        val result = DiffUtil.calculateDiff(diff)
        result.dispatchUpdatesTo(adapter)
    }

    private fun handleLoadWalletSuccess(walletList: WalletList) {
        adapter.reloadItems(walletList.data[0].balances)
        viewModel.updateWallet(walletList)
    }

    private fun handleLoadWalletFail(error: APIError) {
        context?.toast(error.description)
        if (error.code == ErrorCode.USER_AUTH_TOKEN_NOT_FOUND) {
            Storage.clearSession()
            findNavController().navigateUp()
        }
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
        hostActivity.supportActionBar?.title = getString(R.string.balance_title)
    }

    private fun setupRecyclerView() {
        adapter = LoadingRecyclerAdapter(R.layout.viewholder_balance_loading, R.layout.viewholder_balance, viewModel, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        swipeRefresh.setOnRefreshListener {
            viewModel.loadWallet(networkOnly = true)
        }
    }
}
