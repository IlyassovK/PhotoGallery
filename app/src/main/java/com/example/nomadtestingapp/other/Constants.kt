package com.example.nomadtestingapp.other

import java.util.regex.Pattern

object Constants {
    const val BASE_URL = "https://api.inkoda.cloud"

    const val RC_PERMISSION = 111

    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^(?=.*[a-zA-Z]).{6,}\$"
    )
}