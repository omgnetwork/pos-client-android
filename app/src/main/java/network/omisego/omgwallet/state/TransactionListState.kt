package network.omisego.omgwallet.state

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 1/10/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

enum class TransactionListState {
    /* Represents the user doesn't has any transactions*/
    STATE_EMPTY_PAGE,
    /* Represents the transaction's page goes beyond last page. */
    STATE_OUT_BOUND_PAGE,
    /* Represents the transaction page has content to display */
    STATE_CONTENT_PAGE
}
