package network.omisego.omgwallet.setup.util

import network.omisego.omgwallet.setup.extensions.mockEnqueueWithHttpCode
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/3/2019 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
object NetworkMockUtil {
    fun createMockWebServer(): MockWebServer {
        val mockWebServer = MockWebServer()
        mockWebServer.start()
        return mockWebServer
    }

    fun createMockUrl(mockWebServer: MockWebServer): HttpUrl {
        return mockWebServer.url("/api/client/")
    }

    fun enqueue(mockWebServer: MockWebServer, vararg responses: String) {
        responses.forEach { it.mockEnqueueWithHttpCode(mockWebServer) }
    }
}