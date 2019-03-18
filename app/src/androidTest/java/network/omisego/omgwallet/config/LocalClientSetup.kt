package network.omisego.omgwallet.config

import network.omisego.omgwallet.local.test.BuildConfig
import network.omisego.omgwallet.network.ClientSetup

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class LocalClientSetup : ClientSetup {
    override val baseURL: String
        get() = BuildConfig.CONFIG_BASE_URL
    override val socketBaseURL: String
        get() = BuildConfig.CONFIG_SOCKET_BASE_URL
    override val apiKey: String
        get() = BuildConfig.CONFIG_API_KEY
}
