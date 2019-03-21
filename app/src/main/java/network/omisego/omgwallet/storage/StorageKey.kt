package network.omisego.omgwallet.storage

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/8/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

enum class StorageKey(val value: String) {
    KEY_USER("user"),
    KEY_WALLET("wallet"),
    KEY_FINGERPRINT_OPTION("fingerprint_option"),
    KEY_AUTHENTICATION_TOKEN("authentication_token"),
    KEY_USER_EMAIL("user_email"),
    KEY_FINGERPRINT_USER_PASSWORD("fingerprint_user_password"),
    KEY_TOKEN_PRIMARY("token_primary"),
    KEY_TRANSACTION_REQUEST_FORMATTED_ID_RECEIVE("transaction_request_formatted_id_receive"),
    KEY_TRANSACTION_REQUEST_FORMATTED_ID_SEND("transaction_request_formatted_id_send");

    override fun toString() = value
}
