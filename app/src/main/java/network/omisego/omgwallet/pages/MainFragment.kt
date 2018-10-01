package network.omisego.omgwallet.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentMainBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.extension.replaceFragment
import network.omisego.omgwallet.pages.balance.BalanceFragment
import network.omisego.omgwallet.pages.profile.ProfileContainerFragment
import network.omisego.omgwallet.pages.profile.ProfileNavigationViewModel

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var balanceFragment: BalanceFragment
    private lateinit var profileContainerFragment: ProfileContainerFragment
    private lateinit var navigationViewModel: ProfileNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideActivityViewModel()
        balanceFragment = BalanceFragment()
        profileContainerFragment = ProfileContainerFragment()
        navigationViewModel = provideActivityViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(R.layout.fragment_main, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wallets = viewModel.loadWallets()
        val hasAuthToken = viewModel.hasAuthenticationToken()
        when {
            navigationViewModel.liveNavigation.value != null -> {
                init()
                replaceFragment(R.id.pageContainer, profileContainerFragment)
                bottomBarProfile.isSelected = true
            }
            wallets != null -> {
                init()
                replaceFragment(R.id.pageContainer, balanceFragment)
                bottomBarBalance.isSelected = true
            }
            hasAuthToken -> findNavController().navigate(R.id.action_main_to_splashFragment)
            else -> findNavController().navigate(R.id.action_main_to_signInFragment)
        }
    }

    private fun init() {
        bottomBarBalance.setOnClickListener {
            replaceFragment(R.id.pageContainer, balanceFragment)
            navigationViewModel.liveNavigation.value = null
            bottomBarBalance.isSelected = true
            bottomBarProfile.isSelected = false
        }
        bottomBarProfile.setOnClickListener {
            replaceFragment(R.id.pageContainer, profileContainerFragment)
            navigationViewModel.liveNavigation.value = R.layout.fragment_profile
            bottomBarProfile.isSelected = true
            bottomBarBalance.isSelected = false
        }
        fabQR.setOnClickListener { findNavController().navigate(R.id.action_main_to_showQRFragment) }

        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(context!!, R.color.colorGray)
    }
}
