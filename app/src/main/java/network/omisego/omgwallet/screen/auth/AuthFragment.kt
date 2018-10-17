package network.omisego.omgwallet.screen.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_authenticated.*
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.getColor
import network.omisego.omgwallet.extension.provideViewModel

class AuthFragment : Fragment() {

    private lateinit var navigateListener: (NavController, NavDestination) -> Unit
    private lateinit var navController: NavController
    private lateinit var viewModel: AuthViewModel
    private val window: Window by lazy {
        activity?.window!!.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupNavController()
        chooseDestination()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authenticated, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navController.removeOnNavigatedListener(navigateListener)
    }

    private fun chooseDestination() {
        when {
            viewModel.hasTransactionRequestFormattedId() -> {
                // Do nothing
            }
            else -> {
                navController.navigate(R.id.action_global_splash)
            }
        }
    }

    private fun setupNavController() {
        navController = (activity as MainActivity).findNavController(R.id.content)
        toolbar.setupWithNavController(navController)
        bottomNavigation.setupWithNavController(navController)
        fabQR.setOnClickListener { navController.navigate(R.id.action_global_showQR) }
        navigateListener = { _, destination ->
            setBottomNavigationVisibility(destination.id !in arrayOf(R.id.showQR, R.id.splash))
            toolbar.visibility = if (destination.id == R.id.splash) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
        navController.addOnNavigatedListener(navigateListener)
    }

    private fun setBottomNavigationVisibility(visible: Boolean) {
        if (visible) {
            (fabQR as View).visibility = View.VISIBLE
            bottomNavigation.visibility = View.VISIBLE
            toolbar.setBackgroundColor(context getColor R.color.colorGray)
            window.statusBarColor = context getColor R.color.colorGray
        } else {
            (fabQR as View).visibility = View.GONE
            bottomNavigation.visibility = View.INVISIBLE
            toolbar.setBackgroundColor(context getColor R.color.colorBlue)
            window.statusBarColor = context getColor R.color.colorBlue
        }
    }
}