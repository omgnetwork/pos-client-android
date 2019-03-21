package network.omisego.omgwallet.util

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omisego.OMGAPIAdmin
import co.omisego.omisego.OMGAPIClient
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.AdminConfiguration
import co.omisego.omisego.model.Wallet
import co.omisego.omisego.model.params.LoginParams
import co.omisego.omisego.model.params.admin.TransactionCreateParams
import co.omisego.omisego.network.ewallet.EWalletAdmin
import network.omisego.omgwallet.config.TestData
import network.omisego.omgwallet.local.test.BuildConfig
import java.math.BigDecimal

object TestUtil {
    fun ensureEnoughBalances(client: OMGAPIClient, loginParams: LoginParams, target: BigDecimal = 2.bd) {
        client.login(loginParams).execute()
        val responseWallets = client.getWallets().execute()
        val wallet = responseWallets.body()?.data?.data?.get(0)!!
        val enough = wallet.balances[0].amount >= wallet.balances[0].token.subunitToUnit.multiply(target)
        if (!enough) {
            transfer(wallet, target)
        }
    }

    private fun transfer(wallet: Wallet, target: BigDecimal) {
        val adminAPI = getAdminAPI()
        val params = LoginParams(TestData.ADMIN_EMAIL, TestData.ADMIN_PASSWORD)
        adminAPI.login(params).execute()
        val txParam = TransactionCreateParams(
            fromAddress = TestData.MASTER_ACCOUNT_WALLET_ADDRESS,
            toAddress = wallet.address,
            tokenId = wallet.balances[0].token.id,
            amount = wallet.balances[0].token.subunitToUnit.multiply(target)
        )
        adminAPI.createTransaction(txParam).execute()
    }

    private fun getAdminAPI(): OMGAPIAdmin {
        val adminBaseURL = BuildConfig.CONFIG_BASE_URL.replace("client", "admin")
        val adminConfig = AdminConfiguration(adminBaseURL)
        val ewalletAdmin = EWalletAdmin.Builder {
            clientConfiguration = adminConfig
        }.build()
        return OMGAPIAdmin(ewalletAdmin)
    }
}