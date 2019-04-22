package network.omisego.omgwallet.setup.util

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/3/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import androidx.test.platform.app.InstrumentationRegistry
import java.io.InputStream
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ResourceFile(private val fileName: String) : ReadOnlyProperty<Any, String> {
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        val context = InstrumentationRegistry.getInstrumentation().context
        val fileInputStream = context.resources.assets.open(fileName)
        return fileInputStream.toFile()
    }

    private fun InputStream.toFile(): String {
        return this.bufferedReader().use { it.readText() }
    }
}
