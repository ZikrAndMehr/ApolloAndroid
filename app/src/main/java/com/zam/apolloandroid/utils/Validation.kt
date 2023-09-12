package com.zam.apolloandroid.utils

import android.util.Patterns
import com.zam.apolloandroid.ui_layer.signup.SignUpConstants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object Validation {
    private const val REGEX_LETTERS = "^[A-Za-z]+$"
    private const val REGEX_ALPHANUMERICS = "^[A-Za-z\\d]*$"
    private const val REGEX_DATE = "(0?[1-9]|1\\d|2\\d|3[0-1])/(0?[1-9]|1(0-2))/\\d{4}"
    private val SIMPLE_DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy")

    fun isValidEmail(str: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    fun isValidPasswordRange(str: String): Boolean {
        return str.length >= SignUpConstants.PASSWORD_RANGE
    }

    fun isValidLetters(str: String): Boolean {
        return Pattern.compile(REGEX_LETTERS).matcher(str).matches()
    }

    fun isValidAddressRange(str: String): Boolean {
        return str.length in SignUpConstants.ADDRESS_RANGE_MIN..SignUpConstants.ADDRESS_RANGE_MAX
    }

    fun isValidAlphanumerics(str: String): Boolean {
        return Pattern.compile(REGEX_ALPHANUMERICS).matcher(str).matches()
    }

    fun isValidDate(str: String): Boolean {
        if (!Pattern.compile(REGEX_DATE).matcher(str).matches()) return false

        val dateFormat = SIMPLE_DATE_FORMAT
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(str)
            true
        } catch (ex: ParseException) {
            false
        }
    }

    fun isBeforeCurrentDate(dateOfBirth: String): Boolean  {
        val dateFormat = SIMPLE_DATE_FORMAT
        val date = dateFormat.parse(dateOfBirth)
        if (date != null) {
            if (!date.before(Date())) return false
        }
        return true
    }
}