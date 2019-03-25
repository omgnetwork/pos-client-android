package network.omisego.omgwallet.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/9/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun spannable(func: () -> SpannableString) = func()

private fun span(s: CharSequence, o: Any) = (if (s is String) SpannableString(s) else s as? SpannableString
    ?: SpannableString("")).apply { setSpan(o, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }

operator fun SpannableString.plus(s: SpannableString) = SpannableString(TextUtils.concat(this, s))

operator fun SpannableString.plus(s: String) = SpannableString(TextUtils.concat(this, s))

fun bold(s: CharSequence) = span(s, StyleSpan(Typeface.BOLD))
fun color(color: Int, s: CharSequence) = span(s, ForegroundColorSpan(color))
fun click(s: CharSequence, handleClick: ClickableSpan) = span(s, handleClick)
