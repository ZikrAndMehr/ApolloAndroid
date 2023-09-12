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

package com.zam.apolloandroid.ui_layer.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.zam.apolloandroid.R
import com.zam.apolloandroid.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.tilDateSu.setStartIconOnClickListener { showDatePicker() }

        return binding.root
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
                        enableEmailHelperText.collect { showReason: String? ->
                            binding.tilEmailSu.apply {
                                when (showReason) {
                                    SignUpConstants.FIELD_EMPTY -> {
                                        helperText = getString(R.string.email_helper_text_empty)
                                        isHelperTextEnabled = true
                                    }
                                    SignUpConstants.FIELD_WRONG -> {
                                        helperText = getString(R.string.email_helper_text_wrong)
                                        isHelperTextEnabled = true
                                    }
                                    else -> isHelperTextEnabled = false
                                }
                            }
                        }
                    }
                    launch {
                        enablePasswordHelperText.collect { showReason: String? ->
                            binding.tilPasswordSu.apply {
                                when (showReason) {
                                    SignUpConstants.FIELD_EMPTY -> {
                                        helperText = getString(R.string.password_helper_text_empty)
                                        isHelperTextEnabled = true
                                    }
                                    SignUpConstants.PASSWORD_SHORT -> {
                                        helperText = getString(R.string.password_helper_text_range, SignUpConstants.PASSWORD_RANGE)
                                        isHelperTextEnabled = true
                                    }
                                    else -> isHelperTextEnabled = false
                                }
                            }
                        }
                    }
                    launch {
                        enableFirstNameHelperText.collect { showReason: String? ->
                            binding.tilFirstNameSu.apply {
                                when (showReason) {
                                    SignUpConstants.FIELD_EMPTY -> {
                                        helperText = getString(R.string.first_name_helper_text_empty)
                                        isHelperTextEnabled = true
                                    }
                                    SignUpConstants.FIELD_CONTAINS_SPECIAL_CHARACTERS -> {
                                        helperText = getString(R.string.first_name_helper_text_sc)
                                        isHelperTextEnabled = true
                                    }
                                    else -> isHelperTextEnabled = false
                                }
                            }
                        }
                    }
                    launch {
                        enableLastNameHelperText.collect { showReason: String? ->
                            binding.tilLastNameSu.apply {
                                when (showReason) {
                                    SignUpConstants.FIELD_EMPTY -> {
                                        helperText = getString(R.string.last_name_helper_text_empty)
                                        isHelperTextEnabled = true
                                    }
                                    SignUpConstants.FIELD_CONTAINS_SPECIAL_CHARACTERS -> {
                                        helperText = getString(R.string.last_name_helper_text_sc)
                                        isHelperTextEnabled = true
                                    }
                                    else -> isHelperTextEnabled = false
                                }
                            }
                        }
                    }
                    launch {
                        enableAddressHelperText.collect { showReason: String? ->
                            binding.tilAddressSu.apply {
                                when (showReason) {
                                    SignUpConstants.FIELD_EMPTY -> {
                                        helperText = getString(R.string.address_helper_text_empty)
                                        isHelperTextEnabled = true
                                    }
                                    SignUpConstants.FIELD_CONTAINS_SPECIAL_CHARACTERS -> {
                                        helperText = getString(R.string.address_helper_text_sc)
                                        isHelperTextEnabled = true
                                    }
                                    SignUpConstants.ADDRESS_NOT_IN_RANGE -> {
                                        helperText = getString(R.string.address_helper_text_range, SignUpConstants.ADDRESS_RANGE_MIN, SignUpConstants.ADDRESS_RANGE_MAX)
                                        isHelperTextEnabled = true
                                    }
                                    else -> isHelperTextEnabled = false
                                }
                            }
                        }
                    }
                    launch {
                        enableDateHelperText.collect { showReason: String? ->
                            binding.tilDateSu.apply {
                                when (showReason) {
                                    SignUpConstants.FIELD_EMPTY -> {
                                        helperText = getString(R.string.date_helper_text_empty)
                                        isHelperTextEnabled = true
                                    }
                                    SignUpConstants.FIELD_WRONG -> {
                                        helperText = getString(R.string.date_helper_text_sc)
                                        isHelperTextEnabled = true
                                    }
                                    SignUpConstants.DATE_CURRENT -> {
                                        helperText = getString(R.string.date_helper_text_current)
                                        isHelperTextEnabled = true
                                    }
                                    else -> isHelperTextEnabled = false
                                }
                            }
                        }
                    }
                    launch {
                        signedUp.collect { newUser ->
                            when (newUser) {
                                NewUser.SIGNED_UP -> {
                                    val action = SignUpFragmentDirections.actionSignUpFragmentToContinentsFragment()
                                    findNavController().navigate(action)
                                }
                                NewUser.NOT_SIGNED_UP -> {
                                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    launch {
                        navigateToSignInFragment.collect {
                            val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun showDatePicker() {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val picker = MaterialDatePicker.Builder
            .datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraints)
            .build()

        picker.addOnPositiveButtonClickListener{
            dateInLong: Long ->
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            binding.etDateSu.setText(dateFormat.format(dateInLong))
        }

        picker.show(parentFragmentManager, "DATE")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}