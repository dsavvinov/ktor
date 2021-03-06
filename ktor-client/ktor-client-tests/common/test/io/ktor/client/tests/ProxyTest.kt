/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.client.tests

import io.ktor.client.engine.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.tests.utils.*
import kotlinx.serialization.*
import kotlin.test.*

@Serializable
data class ProxyResponse(val status: String)

class ProxyTest : ClientLoader() {

    @Test
    fun testHttpProxy() = clientTests(listOf("Js")) {
        config {
            engine {
                proxy = ProxyBuilder.http(HTTP_PROXY_SERVER)
            }
        }

        test { client ->
            val response = client.get<String>("http://google.com")
            assertEquals("proxy", response)
        }
    }

    @Test
    fun testProxyWithSerialization() = clientTests(listOf("Js")) {
        config {
            engine {
                proxy = ProxyBuilder.http(HTTP_PROXY_SERVER)
            }

            install(JsonFeature)
        }

        test { client ->
            val response = client.get<ProxyResponse>("http://google.com/json")
            val expected = ProxyResponse("ok")

            assertEquals(expected, response)
        }
    }
}
