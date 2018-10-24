package network.omisego.omgwallet.screen.auth.balance.detail

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import co.omisego.omisego.model.Balance
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.ViewholderBalanceDetailBinding
import network.omisego.omgwallet.extension.provideActivityAndroidViewModel
import network.omisego.omgwallet.extension.provideAndroidViewModel

class BalanceDetailItemFragment : Fragment() {

    private lateinit var balance: Balance
    private lateinit var balanceDetailViewModel: BalanceDetailViewModel
    private lateinit var binding: ViewholderBalanceDetailBinding
    private lateinit var viewModel: BalanceDetailItemViewModel
    private var page: Int = 0
    private var totalPage: Int = 0

    companion object {
        const val KEY_BALANCE: String = "balance"
        const val KEY_PAGE: String = "page"
        const val KEY_TOTAL_PAGE: String = "total_page"

        fun create(balance: Balance, page: Int, totalPage: Int): BalanceDetailItemFragment {
            return BalanceDetailItemFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelable(KEY_BALANCE, balance)
                    this.putInt(KEY_PAGE, page)
                    this.putInt(KEY_TOTAL_PAGE, totalPage)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideAndroidViewModel()
        balanceDetailViewModel = provideActivityAndroidViewModel()
        balance = arguments?.getParcelable(KEY_BALANCE)!!
        page = arguments?.getInt(KEY_PAGE)!!
        totalPage = arguments?.getInt(KEY_TOTAL_PAGE)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.viewholder_balance_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setLifecycleOwner(this)
        binding.balance = balance
        binding.page = page
        binding.totalPage = totalPage
        binding.viewModel = viewModel
        binding.balanceDetailViewModel = balanceDetailViewModel
        initTokenPrimary()

        balanceDetailViewModel.liveTokenPrimaryId.observe(this, Observer { tokenPrimaryId ->
            viewModel.resolveTokenPrimaryEnable(balance, tokenPrimaryId)
            viewModel.resolveTokenPrimaryText(balance, tokenPrimaryId)
        })
    }

    private fun initTokenPrimary() {
        viewModel.resolveTokenPrimaryText(balance, balanceDetailViewModel.loadTokenPrimary())
        viewModel.resolveTokenPrimaryEnable(balance, balanceDetailViewModel.loadTokenPrimary())
    }
}