package network.omisego.omgwallet.storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.content.SharedPreferences
import co.omisego.omisego.security.OMGKeyManager
import co.omisego.omisego.utils.GsonProvider
import com.google.gson.Gson
import network.omisego.omgwallet.BuildConfig
import network.omisego.omgwallet.R
import network.omisego.omgwallet.extension.contains
import network.omisego.omgwallet.extension.decryptWith
import network.omisego.omgwallet.extension.encryptWith
import network.omisego.omgwallet.extension.get
import network.omisego.omgwallet.extension.getBoolean
import network.omisego.omgwallet.extension.putBoolean
import network.omisego.omgwallet.extension.remove
import network.omisego.omgwallet.model.Credential
import network.omisego.omgwallet.util.ContextUtil

class Storage(
    private val context: Context = ContextUtil.context,
    val gson: Gson = GsonProvider.create(),
    val sp: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE),
    private val keyManager: OMGKeyManager
) {

    companion object {
        fun create(context: Context): Storage {
            val keyManager = createKeyManager(context)
            val sp = createSharePref(context)
            return Storage(keyManager = keyManager, sp = sp)
        }

        private fun createKeyManager(context: Context): OMGKeyManager {
            return OMGKeyManager.Builder {
                keyAlias = BuildConfig.CONFIG_KEY_ALIAS
                iv = BuildConfig.CONFIG_IV
            }.build(context)
        }

        private fun createSharePref(context: Context): SharedPreferences {
            val name = context.getString(R.string.app_name)
                .toLowerCase()
                .replace(" ", "_")

            return context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }
    }

    fun loadCredential(): Credential {
        val authenticationToken = sp[StorageKey.KEY_AUTHENTICATION_TOKEN]!!
        if (authenticationToken.isEmpty()) {
            return Credential()
        }
        return Credential(
            authenticationToken decryptWith keyManager
        )
    }

    fun has(vararg keys: StorageKey) = keys.all { sp.contains(it) }

    fun getStringRecord(key: StorageKey, default: String? = null): String? {
        if (sp[key].isNullOrEmpty()) return default
        return sp[key]
    }

    fun getBooleanRecord(key: StorageKey, default: Boolean = false): Boolean {
        if (sp[key].isNullOrEmpty()) return default
        return sp.getBoolean(key, default)
    }

    inline fun <reified T> getRecord(key: StorageKey, default: T? = null): T? {
        if (sp[key].isNullOrEmpty()) return default
        return gson.fromJson<T>(sp[key], T::class.java)
    }

    fun putBooleanRecord(kv: Pair<StorageKey, Boolean>) {
        val (key, value) = kv
        sp.edit().putBoolean(key, value).apply()
    }

    fun putRecords(vararg kvs: Pair<StorageKey, String?>) {
        var editor = sp.edit()
        kvs.forEach { (key, value) ->
            editor = if (value == null) {
                editor.remove(key.value)
            } else {
                editor.putString(key.value, value)
            }
        }
        editor.apply()
    }

    fun deleteRecords(vararg keys: StorageKey) {
        var editor = sp.edit()
        keys.forEach { key -> editor = editor.remove(key) }
        editor.apply()
    }

    fun deleteAll() {
        sp.edit().clear().apply()
    }

    fun toJson(model: Any): String {
        return gson.toJson(model)
    }

    fun encrypt(plain: String) = plain encryptWith keyManager

    fun decrypt(encrypted: String) = encrypted decryptWith keyManager
}
