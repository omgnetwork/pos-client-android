package network.omisego.omgwallet.screen.unauth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import network.omisego.omgwallet.R
import network.omisego.omgwallet.databinding.FragmentMainBinding
import network.omisego.omgwallet.extension.bindingInflate
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.screen.MainViewModel

class UnAuthFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideActivityViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(R.layout.fragment_main, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        val isLoggedIn = viewModel.hasAuthenticationToken()
        when {
//            isLoggedIn -> findNavController().navigate(R.id.action_main_to_splashFragment)
//            else -> findNavController().navigate(R.id.action_main_to_signInFragment)
        }
    }

    private fun init() {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(context!!, R.color.colorGray)
    }
}