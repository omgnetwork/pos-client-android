package network.omisego.omgwallet.config

import co.omisego.omisego.OMGAPIClient
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.ClientAuthenticationToken
import co.omisego.omisego.model.ClientConfiguration
import co.omisego.omisego.model.params.LoginParams
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionParams
import co.omisego.omisego.model.transaction.request.TransactionRequestParams
import co.omisego.omisego.network.ewallet.EWalletClient
import okhttp3.OkHttpClient
import java.math.BigDecimal

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class TxConsumerClient(val client: OMGAPIClient, val okHttpClient: OkHttpClient) {

    fun login(): ClientAuthenticationToken? {
        val credential = client.login(LoginParams(TestData.CONSUME_TX_USER_EMAIL, TestData.CONSUME_TX_USER_PASSWORD)).execute().body()
        return credential?.data
    }

    fun consumeTxFormattedId(formattedId: String, amount: BigDecimal = 100.bd): TransactionConsumption? {
        val tx = client.retrieveTransactionRequest(TransactionRequestParams(formattedId)).execute().body()?.data
        val consumeResponse = client.consumeTransactionRequest(TransactionConsumptionParams.create(tx!!, amount)).execute().body()?.data
        return consumeResponse
    }

    companion object {
        fun create(): TxConsumerClient {
            val clientSetup = LocalClientSetup()
            val configuration = ClientConfiguration(
                clientSetup.baseURL,
                clientSetup.apiKey
            )

            val eWalletClient = EWalletClient.Builder {
                clientConfiguration = configuration
                debug = true
            }.build()

            val txConsumerClient = TxConsumerClient(OMGAPIClient(eWalletClient), eWalletClient.client)
            txConsumerClient.login()
            return txConsumerClient
        }
    }
}