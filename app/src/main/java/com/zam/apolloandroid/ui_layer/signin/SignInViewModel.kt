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