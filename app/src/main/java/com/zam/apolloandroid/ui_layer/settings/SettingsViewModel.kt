package com.zam.apolloandroid.ui_layer.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zam.apolloandroid.data_layer.user.UserRepository
import com.zam.apolloandroid.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private var _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    private var _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName
    private var _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName
    private var _address = MutableStateFlow("")
    val address: StateFlow<String> = _address
    private var _dateOfBirth = MutableStateFlow("")
    val dateOfBirth: StateFlow<String>  = _dateOfBirth
    private var _userDataLoaded = MutableSharedFlow<UserData>()
    val userDataLoaded: SharedFlow<UserData> = _userDataLoaded
    private var _navigateToSignInFragment = MutableSharedFlow<Boolean>()
    val navigateToSignInFragment: SharedFlow<Boolean> = _navigateToSignInFragment

    init {
        viewModelScope.launch {
            val userData = repository.fetchUserData()
            if (userData == null) _userDataLoaded.emit(UserData.ERROR)
            else {
                _userDataLoaded.emit(UserData.LOADED)
                userData.apply {
                    _email.value = getValue(AppConstants.FIRESTORE_USER_EMAIL_FIELD)
                    _firstName.value = getValue(AppConstants.FIRESTORE_USER_FIRST_NAME_FIELD)
                    _lastName.value = getValue(AppConstants.FIRESTORE_USER_LAST_NAME_FIELD)
                    _address.value = getValue(AppConstants.FIRESTORE_USER_ADDRESS_FIELD)
                    _dateOfBirth.value = getValue(AppConstants.FIRESTORE_USER_DATE_OF_BIRTH_FIELD)
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOutUser()
            _navigateToSignInFragment.emit(true)
        }
    }
}