package com.zam.apolloandroid.utils

import countries.GetContinentsQuery
import countries.GetCountriesOfContinentQuery

typealias ApolloCountry = GetCountriesOfContinentQuery.Country
typealias ApolloContinent = GetContinentsQuery.Continent

object AppConstants {
    const val COUNTRIES_API_URL = "https://countries.trevorblades.com"

    const val APP_LANGUAGE = "APP_LANGUAGE"
    const val ENGLISH_LANGUAGE = "en"
    const val POLISH_LANGUAGE = "pl"

    const val SETTINGS_PREFERENCE_FILE_KEY ="com.zam.apolloandroid.SETTINGS_PREFERENCE_FILE_KEY"
    const val APP_THEME ="APP_THEME"
    const val THEME_LIGHT = "MODE_NIGHT_NO"
    const val THEME_DARK = "MODE_NIGHT_YES"
    const val THEME_FOLLOW_SYSTEM= "MODE_NIGHT_FOLLOW_SYSTEM"
    const val THEME_AUTO= "MODE_NIGHT_AUTO"
    const val THEME_CUSTOM= "MODE_NIGHT_CUSTOM"

    const val FIRESTORE_MAIN_COLLECTION_PATH = "users_data"
    const val FIRESTORE_USER_EMAIL_FIELD = "email"
    const val FIRESTORE_USER_FIRST_NAME_FIELD = "first_name"
    const val FIRESTORE_USER_LAST_NAME_FIELD = "last_name"
    const val FIRESTORE_USER_ADDRESS_FIELD = "address"
    const val FIRESTORE_USER_DATE_OF_BIRTH_FIELD = "date_of_birth"

    const val FIRESTORE_COUNTRIES_COLLECTION_PATH = "users_data/%1\$s/continents/%2\$s/countries"
    const val FIRESTORE_COUNTRY_EMOJI_FIELD = "emoji"
    const val FIRESTORE_COUNTRY_NAME_FIELD = "name"
    const val FIRESTORE_COUNTRY_NATIVE_NAME_FIELD = "native"
    const val FIRESTORE_COUNTRY_STARRED_FIELD = "starred"
}