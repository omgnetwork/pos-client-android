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
import androidx.navigation.fragment.findNavController
import co.omisego.omisego.model.Balance
import kotlinx.android.synthetic.main.fragment_balance_detail.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentBalanceDetailBinding
import network.omisego.omgwallet.extension.provideActivityAndroidViewModel
import network.omisego.omgwallet.util.EventObserver

class BalanceDetailFragment : Fragment() {
    private lateinit var binding: FragmentBalanceDetailBinding
    private lateinit var viewModel: BalanceDetailViewModel
    private lateinit var balances: List<Balance>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideActivityAndroidViewModel()
        balances = viewModel.loadBalances()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_balance_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        viewModel.liveEventTokenPrimaryId.observe(this, EventObserver { primaryTokenId ->
            val dirGlobalSplash = viewModel.provideSplashDirection(primaryTokenId)
            findNavController().navigate(dirGlobalSplash)
        })
    }

    private fun setupViewPager() {
        viewpager.adapter = BalanceDetailPagerAdapter(balances, childFragmentManager)
        pageIndicatorView.setFadeOnIdle(true)

        val currentPage = BalanceDetailFragmentArgs.fromBundle(arguments).tokenIndex
        viewpager.setCurrentItem(currentPage, true)
        pageIndicatorView.setSelected(currentPage)
    }
}
