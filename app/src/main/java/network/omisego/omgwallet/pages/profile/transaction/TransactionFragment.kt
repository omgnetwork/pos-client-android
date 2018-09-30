package network.omisego.omgwallet.pages.profile.transaction

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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentTransactionBinding
import network.omisego.omgwallet.extension.getDrawableCompat
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.pages.profile.ProfileNavigationViewModel

class TransactionFragment : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var navigationViewModel: ProfileNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationViewModel = provideActivityViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        val hostActivity = activity as AppCompatActivity
        hostActivity.setSupportActionBar(toolbar)
        toolbar.navigationIcon = context?.getDrawableCompat(R.drawable.ic_arrow_back)
        toolbar.title = getString(R.string.transaction_list_title)
        toolbar.setNavigationOnClickListener { navigationViewModel.liveNavigation.value = R.layout.fragment_profile }
    }
}
