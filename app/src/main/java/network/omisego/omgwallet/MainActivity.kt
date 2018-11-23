package network.omisego.omgwallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import co.omisego.omisego.model.ClientAuthenticationToken
import network.omisego.omgwallet.extension.provideViewModel
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.network.ClientProvider
import network.omisego.omgwallet.util.RepositoryUtil

class MainActivity : AppCompatActivity(), LoginListener {

    private lateinit var globalViewModel: GlobalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.OMGTheme)
        setContentView(R.layout.activity_main)
        globalViewModel = provideViewModel()
        observeEvent()
    }

    private fun observeEvent() {
        globalViewModel.liveAuthenticationToken.observe(this, Observer { authenticationToken ->
            if (authenticationToken == null) {
                globalViewModel.stopListenForUserEvent(ClientProvider.socketClient!!)
                RepositoryUtil.localRepository.clearSession()
                ClientProvider.socketClient = null
            } else {
                ClientProvider.initSocketClient(authenticationToken)
                globalViewModel.startListenForUserEvent(ClientProvider.socketClient!!)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        ClientProvider.socketClient?.let {
            globalViewModel.startListenForUserEvent(it)
        }
    }

    override fun onStop() {
        ClientProvider.socketClient?.let {
            globalViewModel.stopListenForUserEvent(it)
        }
        super.onStop()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host).navigateUp()

    override fun onBackPressed() {
        if (!findNavController(R.id.nav_host).popBackStack()) {
            super.onBackPressed()
        }
    }

    override fun onLoggedin(email: String, token: ClientAuthenticationToken) {
        val credential = Credential(token.authenticationToken)
        RepositoryUtil.localRepository.clearOldAccountCache(email)
        RepositoryUtil.localRepository.saveUserEmail(email)
        RepositoryUtil.localRepository.saveUser(token.user)
        RepositoryUtil.localRepository.saveCredential(credential)
        globalViewModel.liveAuthenticationToken.value = credential.authenticationToken
    }

    override fun onLoggedout() {
        globalViewModel.liveAuthenticationToken.value = null
    }
}

interface LoginListener {
    fun onLoggedin(email: String, token: ClientAuthenticationToken)
    fun onLoggedout()
}
