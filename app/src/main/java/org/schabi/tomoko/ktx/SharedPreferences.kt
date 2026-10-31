package org.coffe.tomoko.ktx

import android.content.SharedPreferences

fun SharedPreferences.getStringSafe(key: String, defValue: String): String {
    return getString(key, null) ?: defValue
}
