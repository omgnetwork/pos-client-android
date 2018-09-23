package network.omisego.omgwallet.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentMainBinding
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.extension.replaceFragment
import network.omisego.omgwallet.pages.balance.BalanceFragment
import network.omisego.omgwallet.pages.profile.ProfileFragment

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var balanceFragment: BalanceFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideActivityViewModel()
        balanceFragment = BalanceFragment()
        profileFragment = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wallets = viewModel.loadWallets()
        val userEmail = viewModel.loadUserEmail()
        when {
            wallets != null -> {
                init()
                replaceFragment(R.id.pageContainer, balanceFragment)
                bottomBarBalance.isSelected = true
            }
            !userEmail.isNullOrEmpty() -> findNavController().navigate(R.id.action_main_to_splashFragment)
            else -> findNavController().navigate(R.id.action_main_to_signInFragment)
        }
    }

    private fun init() {
        bottomBarBalance.setOnClickListener {
            replaceFragment(R.id.pageContainer, balanceFragment)
            bottomBarBalance.isSelected = true
            bottomBarProfile.isSelected = false
        }
        bottomBarProfile.setOnClickListener {
            replaceFragment(R.id.pageContainer, profileFragment)
            bottomBarProfile.isSelected = true
            bottomBarBalance.isSelected = false
        }
        fabQR.setOnClickListener { findNavController().navigate(R.id.action_main_to_showQRFragment) }
    }
}
