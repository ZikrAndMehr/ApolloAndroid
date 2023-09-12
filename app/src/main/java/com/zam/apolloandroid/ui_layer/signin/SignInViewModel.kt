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

package com.zam.apolloandroid.ui_layer.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zam.apolloandroid.data_layer.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    private var _signedIn = MutableStateFlow(CurrentUser.UNKNOWN)
    val signedIn: StateFlow<CurrentUser> = _signedIn
    private var _navigateToForgotPasswordFragment = MutableSharedFlow<Boolean>()
    val navigateToForgotPasswordFragment: SharedFlow<Boolean> = _navigateToForgotPasswordFragment
    private var _navigateToSignUpFragment = MutableSharedFlow<Boolean>()
    val navigateToSignUpFragment: SharedFlow<Boolean> = _navigateToSignUpFragment

    fun checkCurrentUser() {
        viewModelScope.launch {
            if (repository.checkCurrentUser() != null) {
                _signedIn.emit(CurrentUser.SIGNED_IN)
            }
        }
    }

    fun forgotPassword() {
        viewModelScope.launch {
            _navigateToForgotPasswordFragment.emit(true)
        }
    }

    fun signInExistingUsers(email: String, password: String) {
        if (checkEmailAndPassword(email, password)) {
            viewModelScope.launch {
                val user = repository.signInExistingUser(email, password)
                _signedIn.emit(if (user != null) CurrentUser.SIGNED_IN else CurrentUser.NOT_SIGNED_IN)
            }
        }
    }

    private fun checkEmailAndPassword(email: String, password: String): Boolean {
        if (email.isEmpty()) return false
        else if (password.isEmpty()) return false
        return true
    }

    fun signUp() {
        viewModelScope.launch {
            _navigateToSignUpFragment.emit(true)
        }
    }
}