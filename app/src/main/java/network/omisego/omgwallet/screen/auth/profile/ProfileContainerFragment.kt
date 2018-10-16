package network.omisego.omgwallet.screen.auth.profile

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
import network.omisego.omgwallet.databinding.FragmentProfileContainerBinding
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.extension.replaceFragment
import network.omisego.omgwallet.screen.auth.profile.main.ProfileFragment
import network.omisego.omgwallet.screen.auth.profile.transaction.TransactionListFragment

class ProfileContainerFragment : Fragment() {
    private lateinit var profileFragment: ProfileFragment
    private lateinit var viewModel: ProfileNavigationViewModel
    private lateinit var binding: FragmentProfileContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileFragment = ProfileFragment()
        viewModel = provideActivityViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_container, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(R.id.profileContainer, profileFragment)
        viewModel.liveNavigation.observe(this, Observer {
            when (it) {
                R.layout.fragment_transaction -> {
                    replaceFragment(R.id.profileContainer, TransactionListFragment())
                }
                else -> {
                    replaceFragment(R.id.profileContainer, profileFragment)
                }
            }
        })
    }
}
