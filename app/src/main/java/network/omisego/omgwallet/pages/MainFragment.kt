package network.omisego.omgwallet.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentMainBinding
import network.omisego.omgwallet.extension.provideActivityViewModel

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideActivityViewModel()
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
            }
            !userEmail.isNullOrEmpty() -> findNavController().navigate(R.id.action_main_to_splashFragment)
            else -> findNavController().navigate(R.id.action_main_to_signInFragment)
        }
    }
}
