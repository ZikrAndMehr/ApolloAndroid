/*
 * Copyright (C) 2023 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zam.apolloandroid

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.zam.apolloandroid.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setAppPreferences()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun setAppPreferences() {
        val sharedPref = getSharedPreferences(AppConstants.SETTINGS_PREFERENCE_FILE_KEY,Context.MODE_PRIVATE)
        val currentUiMode = sharedPref.getString(AppConstants.APP_THEME, AppConstants.THEME_LIGHT)

        currentUiMode?.let { mode ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                val uiMode = UiModeManager::class.java.getDeclaredField(mode).getInt(Field::class.java)
                uiModeManager.setApplicationNightMode(uiMode)
            } else {
                val uiMode = AppCompatDelegate::class.java.getDeclaredField(mode).getInt(Field::class.java)
                AppCompatDelegate.setDefaultNightMode(uiMode)
            }
        }

        val currentLanguage = sharedPref.getString(AppConstants.APP_LANGUAGE, AppConstants.ENGLISH_LANGUAGE)

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(currentLanguage)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}