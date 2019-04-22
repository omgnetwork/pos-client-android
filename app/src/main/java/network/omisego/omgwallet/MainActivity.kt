package network.omisego.omgwallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import co.omisego.omisego.OMGAPIClient
import network.omisego.omgwallet.extension.provideViewModel
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.repository.RemoteRepository
import network.omisego.omgwallet.state.SocketState
import network.omisego.omgwallet.util.RepositoryUtil

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.OMGTheme)
        setContentView(R.layout.activity_main)
        val client = initializeClient()
        appViewModel = provideViewModel()
        appViewModel.setClient(client)
        appViewModel.setAuthenticationToken(appViewModel.loadAuthenticationToken())
        observeSocketState()
    }

    private fun initializeClient(): OMGAPIClient {
        val authenticationToken = RepositoryUtil.localRepository.loadAuthenticationToken()
        val eWalletClient = ClientProvider.createClient(authenticationToken)
        val client = OMGAPIClient(eWalletClient)
        RepositoryUtil.remoteRepository = RemoteRepository(client)
        return client
    }

    private fun observeSocketState() {
        appViewModel.liveSocketState.observe(this, Observer { state ->
            when (state) {
                is SocketState.Start -> {
                    val socketClient = ClientProvider.createSocketClient(state.authToken)
                    appViewModel.setSocketClient(socketClient)
                    appViewModel.startListenForUserEvent()
                }
                is SocketState.Stop -> {
                    appViewModel.stopListenForUserEvent()
                    appViewModel.setSocketClient(null)
                }
                is SocketState.Idle -> {
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        appViewModel.startListenForUserEvent()
    }

    override fun onStop() {
        appViewModel.stopListenForUserEvent()
        super.onStop()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host).navigateUp()

    override fun onBackPressed() {
        if (!findNavController(R.id.nav_host).popBackStack()) {
            super.onBackPressed()
        }
    }
}
