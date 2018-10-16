package network.omisego.omgwallet.screen.auth.balance

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentBalanceContainerBinding
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.extension.replaceFragment
import network.omisego.omgwallet.screen.auth.balance.detail.BalanceDetailFragment

class BalanceContainerFragment : Fragment() {
    private lateinit var balanceFragment: BalanceFragment
    private lateinit var viewModel: BalanceNavigationViewModel
    private lateinit var binding: FragmentBalanceContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        balanceFragment = BalanceFragment()
        viewModel = provideActivityViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_balance_container, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        replaceFragment(R.id.balanceContainer, balanceFragment)
//        viewModel.liveNavigation.observe(this, Observer {
//            when (it) {
//                R.layout.fragment_balance_detail -> {
//                    replaceFragment(R.id.balanceContainer, BalanceDetailFragment.create(viewModel.lastPageSelected))
//                }
//                else -> {
//                    replaceFragment(R.id.balanceContainer, balanceFragment)
//                }
//            }
//        })
    }
}
