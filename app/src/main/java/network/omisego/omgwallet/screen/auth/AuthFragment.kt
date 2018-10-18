package network.omisego.omgwallet.screen.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_authenticated.*
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.getColor
import network.omisego.omgwallet.extension.logi
import network.omisego.omgwallet.extension.provideActivityViewModel

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
        viewModel = provideActivityViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupNavController()
        listenForSocketEvent()
    }

    override fun onStart() {
        super.onStart()
        showSplashIfNeeded()
        viewModel.startListenForUserEvent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_authenticated, container, false)
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopListenForUserEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navController.removeOnNavigatedListener(navigateListener)
    }

    private fun listenForSocketEvent() {
        viewModel.liveConsumptionRequestEvent.observe(this, ConsumptionRequestObserver())
        viewModel.liveConsumptionRequestFailEvent.observe(this, ConsumptionRequestFailObserver())
        viewModel.liveConsumptionFinalizedEvent.observe(this, ConsumptionFinalizedObserver())
        viewModel.liveConsumptionFinalizedFailEvent.observe(this, ConsumptionFinalizedFailObserver())
    }

    private fun showSplashIfNeeded() {
        if (!viewModel.hasTransactionRequestFormattedId()) {
            navController.navigate(R.id.action_global_splash)
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

    inner class ConsumptionRequestObserver : Observer<TransactionConsumption> {
        override fun onChanged(txConsumption: TransactionConsumption) {
            val message = "${txConsumption.transaction?.to?.account?.name} will be taken your token ${txConsumption.token.symbol} for amount ${txConsumption.estimatedConsumptionAmount.div(txConsumption.token.subunitToUnit)}"
            logi(message)
        }
    }

    inner class ConsumptionRequestFailObserver : Observer<APIError> {
        override fun onChanged(error: APIError) {
            logi("Consumption error: ${error.description}")
        }
    }

    inner class ConsumptionFinalizedObserver : Observer<TransactionConsumption> {
        override fun onChanged(txConsumption: TransactionConsumption) {
            /* Show notification */
            val message = "${txConsumption.transaction?.from?.account?.name} is given the token ${txConsumption.token.symbol} for amount ${txConsumption.estimatedConsumptionAmount.div(txConsumption.token.subunitToUnit)}"
            logi(message)
            navController.popBackStack(R.id.balance, true)
            navController.navigate(R.id.action_global_balance)
            Snackbar.make(bottomNavigation, message, Snackbar.LENGTH_LONG).show()
        }
    }

    inner class ConsumptionFinalizedFailObserver : Observer<APIError> {
        override fun onChanged(error: APIError) {
            logi("ConsumptionFinalized error: ${error.description}")
        }
    }
}