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
import kotlinx.android.synthetic.main.fragment_balance_detail.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentBalanceDetailBinding
import network.omisego.omgwallet.storage.Storage

class BalanceDetailFragment : Fragment() {
    private var currentPage: Int = 0
    private lateinit var binding: FragmentBalanceDetailBinding

    companion object {
        const val CURRENT_PAGE = "current_page"

        fun create(currentPage: Int): BalanceDetailFragment {
            return BalanceDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(CURRENT_PAGE, currentPage)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPage = arguments!!.getInt(CURRENT_PAGE, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_balance_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val balances = Storage.loadWallets()?.data?.get(0)?.balances!!
        viewpager.adapter = BalanceDetailPagerAdapter(balances, childFragmentManager)
        pageIndicatorView.setFadeOnIdle(true)
        viewpager.setCurrentItem(currentPage, true)
        pageIndicatorView.setSelected(currentPage)
    }
}
