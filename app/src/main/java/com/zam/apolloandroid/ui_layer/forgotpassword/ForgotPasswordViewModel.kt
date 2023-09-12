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

package com.zam.apolloandroid.ui_layer.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zam.apolloandroid.data_layer.user.UserRepository
import com.zam.apolloandroid.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private var _sentPasswordReset = MutableSharedFlow<Boolean>()
    val sentPasswordReset: SharedFlow<Boolean> = _sentPasswordReset
    private var _navigateToSignInFragment = MutableSharedFlow<Boolean>()
    val navigateToSignInFragment: SharedFlow<Boolean> = _navigateToSignInFragment
    private var _navigateToSignUpFragment = MutableSharedFlow<Boolean>()
    val navigateToSignUpFragment: SharedFlow<Boolean> = _navigateToSignUpFragment

    fun sendPasswordReset(email: String) {
        if (checkEmail(email)) {
            viewModelScope.launch {
                _sentPasswordReset.emit(repository.sendPasswordReset(email))
            }
        }
    }

    private fun checkEmail(email: String): Boolean {
        if (email.isEmpty() || Validation.isValidEmail(email)) return false
        return true
    }

    fun signIn() {
        viewModelScope.launch {
            _navigateToSignInFragment.emit(true)
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _navigateToSignUpFragment.emit(true)
        }
    }
}