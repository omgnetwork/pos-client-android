package network.omisego.omgwallet.config

import network.omisego.omgwallet.BuildConfig
import java.util.UUID

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object TestData {
    const val MASTER_ACCOUNT_WALLET_ADDRESS = BuildConfig.CONFIG_TEST_MASTER_ACC_WALLET_ADDRESS
    const val ADMIN_EMAIL = BuildConfig.CONFIG_TEST_ADMIN_EMAIL
    const val ADMIN_PASSWORD = BuildConfig.CONFIG_TEST_ADMIN_PASSWORD

    const val USER_EMAIL = BuildConfig.CONFIG_TEST_USER_EMAIL
    const val USER_PASSWORD = BuildConfig.CONFIG_TEST_USER_PASSWORD

    const val CONSUME_TX_USER_EMAIL = BuildConfig.CONFIG_TEST_CONSUMER_EMAIL
    const val CONSUME_TX_USER_PASSWORD = BuildConfig.CONFIG_TEST_CONSUMER_PASSWORD

    val REGISTER_USER_EMAIL = "test${UUID.randomUUID().toString().take(5)}@omise.co"
}
