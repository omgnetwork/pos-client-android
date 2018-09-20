package network.omisego.omgwallet.pages.confirm

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import network.omisego.omgwallet.pages.confirm.databinding.FragmentConfirmBinding

class ConfirmFragment : Fragment() {
    private lateinit var binding: FragmentConfirmBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm, container, false)
        return binding.root
    }
}
