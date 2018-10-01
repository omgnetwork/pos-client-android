package network.omisego.omgwallet.state

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 1/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
enum class FingerprintDialogState {
    /* Represents the dialog is cancelled */
    STATE_CANCELED,
    /* Represents the password is incorrect */
    STATE_WRONG_PASSWORD,
    /* Represents the fingerprint option is enabled successfully */
    STATE_ENABLED,
}