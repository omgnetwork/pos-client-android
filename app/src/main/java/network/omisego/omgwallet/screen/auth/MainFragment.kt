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
import co.omisego.omisego.model.TransactionConsumption
import co.omisego.omisego.model.TransactionConsumptionStatus
import co.omisego.omisego.model.TransactionRequestType
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import network.omisego.omgwallet.AppViewModel
import network.omisego.omgwallet.GraphMainDirections
import network.omisego.omgwallet.MainActivity
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.calledName
import network.omisego.omgwallet.extension.formatAmount
import network.omisego.omgwallet.extension.getColor
import network.omisego.omgwallet.extension.logi
import network.omisego.omgwallet.extension.provideActivityViewModel
import network.omisego.omgwallet.extension.scaleAmount
import network.omisego.omgwallet.extension.snackbar
import network.omisego.omgwallet.screen.auth.balance.BalanceViewModel

class MainFragment : Fragment() {

    private lateinit var navigateListener: (NavController, NavDestination) -> Unit
    private lateinit var navController: NavController
    private lateinit var viewModel: MainViewModel
    private lateinit var balanceViewModel: BalanceViewModel
    private lateinit var appViewModel: AppViewModel
    private lateinit var snackbar: Snackbar
    private val hostActivity: MainActivity
        get() = (activity as MainActivity)
    private val window: Window by lazy {
        activity?.window!!.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideActivityViewModel()
        balanceViewModel = provideActivityViewModel()
        appViewModel = provideActivityViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupNavController()
        listenForSocketEvent()
    }

    override fun onStart() {
        super.onStart()
        showSplashIfNeeded()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navController.removeOnNavigatedListener(navigateListener)
    }

    private fun listenForSocketEvent() {
        appViewModel.liveConsumptionRequestEvent.observe(this, ConsumptionRequestObserver())
        appViewModel.liveConsumptionRequestFailEvent.observe(this, ConsumptionRequestFailObserver())
        appViewModel.liveConsumptionFinalizedEvent.observe(this, ConsumptionFinalizedObserver())
        appViewModel.liveConsumptionFinalizedFailEvent.observe(this, ConsumptionFinalizedFailObserver())
    }

    private fun showSplashIfNeeded() {
        if (!viewModel.hasTransactionRequestFormattedId()) {
            navController.navigate(viewModel.provideSplashDirection())
        }
    }

    private fun setupNavController() {
        navController = hostActivity.findNavController(R.id.content)
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
            navController.navigate(GraphMainDirections.actionGlobalConfirmTransactionRequest(txConsumption))
            val message = "${txConsumption.transaction?.to?.account?.name} will be taken your token ${txConsumption.token.symbol} for amount ${txConsumption.estimatedRequestAmount.div(txConsumption.transactionRequest.token.subunitToUnit)}"
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
            val message: String
            when (txConsumption.status) {
                TransactionConsumptionStatus.CONFIRMED,
                TransactionConsumptionStatus.APPROVED -> {
                    val templateRes = if (txConsumption.transactionRequest.type == TransactionRequestType.SEND) {
                        R.string.notification_transaction_approved_sent
                    } else {
                        R.string.notification_transaction_approved_received
                    }
                    message = getString(
                        templateRes,
                        txConsumption.scaleAmount().formatAmount(),
                        txConsumption.transactionRequest.token.symbol,
                        txConsumption.calledName()
                    )
                    snackbar = bottomNavigation.snackbar(message)
                    snackbar.show()
                }
                TransactionConsumptionStatus.REJECTED -> {
                    message = getString(
                        R.string.notification_transaction_rejected,
                        txConsumption.calledName()
                    )
                    snackbar = bottomNavigation.snackbar(message)
                    snackbar.show()
                }
            }
            if (txConsumption.transactionRequest.requireConfirmation) {
                navController.popBackStack(R.id.balance, true)
                navController.navigate(R.id.action_global_balance)
            }
            balanceViewModel.loadWallet()
        }
    }

    inner class ConsumptionFinalizedFailObserver : Observer<APIError> {
        override fun onChanged(error: APIError) {
            logi("ConsumptionFinalized error: ${error.description}")
        }
    }
}