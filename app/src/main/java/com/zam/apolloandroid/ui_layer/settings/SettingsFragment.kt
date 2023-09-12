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

package com.zam.apolloandroid.ui_layer.settings

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.zam.apolloandroid.R
import com.zam.apolloandroid.databinding.FragmentSettingsBinding
import com.zam.apolloandroid.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.reflect.Field

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()
    private val sharedPref = lazy { activity?.getSharedPreferences(AppConstants.SETTINGS_PREFERENCE_FILE_KEY,Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val currentLanguage = sharedPref.value?.getString(AppConstants.APP_LANGUAGE, AppConstants.ENGLISH_LANGUAGE)

        binding.chGroupLanguage.apply {
             check(
                if (currentLanguage == AppConstants.ENGLISH_LANGUAGE) R.id.ch_english
                else R.id.ch_polish
            )
            setOnCheckedStateChangeListener { _, checkedIds ->
                changeLanguage(checkedIds[0])
            }
        }

        val currentUiMode = sharedPref.value?.getString(AppConstants.APP_THEME, AppConstants.THEME_LIGHT)

        binding.tbTheme.apply {
            setSpecificButtons()
            when (currentUiMode) {
                AppConstants.THEME_LIGHT -> check(R.id.btn_light)
                AppConstants.THEME_DARK -> check(R.id.btn_dark)
                AppConstants.THEME_FOLLOW_SYSTEM -> check(R.id.btn_system_default)
                AppConstants.THEME_AUTO -> check(R.id.btn_auto)
                AppConstants.THEME_CUSTOM -> check(R.id.btn_custom)
            }
            addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.btn_light -> changeTheme(AppConstants.THEME_LIGHT)
                        R.id.btn_dark -> changeTheme(AppConstants.THEME_DARK)
                        R.id.btn_system_default -> changeTheme(AppConstants.THEME_FOLLOW_SYSTEM)
                        R.id.btn_auto -> changeTheme(AppConstants.THEME_AUTO)
                        R.id.btn_custom -> changeTheme(AppConstants.THEME_CUSTOM)
                    }
                }
            }
        }

        return binding.root
    }

    private fun changeLanguage(chipId: Int?) {
        val language = if (chipId == R.id.ch_english) AppConstants.ENGLISH_LANGUAGE
        else AppConstants.POLISH_LANGUAGE

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)

        sharedPref.value?.edit()?.apply {
            putString(AppConstants.APP_LANGUAGE, language)
            apply()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        activity?.recreate()
    }

    private fun setSpecificButtons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.apply {
                btnSystemDefault.visibility = View.GONE
                btnAuto.visibility = View.VISIBLE
                btnCustom.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                btnSystemDefault.visibility = View.VISIBLE
                btnAuto.visibility = View.GONE
                btnCustom.visibility = View.GONE
            }
        }
    }

    private fun changeTheme(mode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiModeManager = context?.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            val uiMode = UiModeManager::class.java.getDeclaredField(mode).getInt(Field::class.java)
            uiModeManager.setApplicationNightMode(uiMode)
        } else {
            val uiMode = AppCompatDelegate::class.java.getDeclaredField(mode).getInt(Field::class.java)
            AppCompatDelegate.setDefaultNightMode(uiMode)
        }

        sharedPref.value?.edit()?.apply {
            putString(AppConstants.APP_THEME, mode)
            apply()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.apply {
                    launch {
                        userDataLoaded.collect { userData ->
                            when (userData) {
                                UserData.LOADED -> {}
                                UserData.ERROR -> Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    launch {
                        navigateToSignInFragment.collect {
                            val action = SettingsFragmentDirections.actionSettingsFragmentToSignInFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}