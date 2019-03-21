package network.omisego.omgwallet.network

import network.omisego.omgwallet.BuildConfig

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 5/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class APIClientConfig : ClientConfig {
    override val baseURL: String
        get() = BuildConfig.CONFIG_BASE_URL
    override val socketBaseURL: String
        get() = BuildConfig.CONFIG_SOCKET_BASE_URL
    override val apiKey: String
        get() = BuildConfig.CONFIG_API_KEY
}