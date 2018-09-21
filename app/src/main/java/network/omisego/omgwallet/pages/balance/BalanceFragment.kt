package network.omisego.omgwallet.pages.balance

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentBalanceBinding

class BalanceFragment : Fragment() {
    private lateinit var binding: FragmentBalanceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_balance, container, false)
        return binding.root
    }
}
