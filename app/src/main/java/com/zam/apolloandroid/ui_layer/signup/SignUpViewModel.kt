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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zam.apolloandroid.data_layer.user.UserRepository
import com.zam.apolloandroid.utils.AppConstants
import com.zam.apolloandroid.utils.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private var _signedUp = MutableSharedFlow<NewUser>()
    val signedUp: SharedFlow<NewUser> = _signedUp
    private var _navigateToSignInFragment = MutableSharedFlow<Boolean>()
    val navigateToSignInFragment: SharedFlow<Boolean> = _navigateToSignInFragment
    private var _enableEmailHelperText = MutableStateFlow<String?>(null)
    val enableEmailHelperText: StateFlow<String?> = _enableEmailHelperText
    private var _enablePasswordHelperText = MutableStateFlow<String?>(null)
    val enablePasswordHelperText: StateFlow<String?> = _enablePasswordHelperText
    private var _enableFirstNameHelperText = MutableStateFlow<String?>(null)
    val enableFirstNameHelperText: StateFlow<String?> = _enableFirstNameHelperText
    private var _enableLastNameHelperText = MutableStateFlow<String?>(null)
    val enableLastNameHelperText: StateFlow<String?> = _enableLastNameHelperText
    private var _enableAddressHelperText = MutableStateFlow<String?>(null)
    val enableAddressHelperText: StateFlow<String?> = _enableAddressHelperText
    private var _enableDateHelperText = MutableStateFlow<String?>(null)
    val enableDateHelperText: StateFlow<String?> = _enableDateHelperText

    fun signUpNewUsers(email: String, password: String, firstName: String,
                       lastName: String, address: String, dateOfBirth: String) {
        if (checkAllSignUpFields(email, password, firstName, lastName, address, dateOfBirth)) {
            viewModelScope.launch {
                val user = repository.signUpNewUser(email, password)
                if (user != null) {
                    val userDataSaved = repository.saveUserData(getUserData(email, firstName, lastName, address, dateOfBirth))
                    _signedUp.emit(if (userDataSaved) NewUser.SIGNED_UP else NewUser.NOT_SIGNED_UP)
                }
            }
        }
    }

     private fun checkAllSignUpFields(
         email: String, password: String, firstName: String,
         lastName: String, address: String, dateOfBirth: String
     ): Boolean {
         var result = true

         if (email.isEmpty()) {
             _enableEmailHelperText.value = SignUpConstants.FIELD_EMPTY
             result = false
         }
         if (email.isNotEmpty() && !Validation.isValidEmail(email)) {
             _enableEmailHelperText.value = SignUpConstants.FIELD_WRONG
         }
         if (email.isNotEmpty() && Validation.isValidEmail(email))
             _enableEmailHelperText.value = SignUpConstants.FIELD_CORRECT

         if (password.isEmpty()) {
             _enablePasswordHelperText.value = SignUpConstants.FIELD_EMPTY
             result = false
         }
         if (password.isNotEmpty() && !Validation.isValidPasswordRange(password))  {
             _enablePasswordHelperText.value = SignUpConstants.PASSWORD_SHORT
             result = false
         }
         if (password.isNotEmpty() && Validation.isValidPasswordRange(password))
             _enablePasswordHelperText.value = SignUpConstants.FIELD_CORRECT

         if (firstName.isEmpty()) {
             _enableFirstNameHelperText.value = SignUpConstants.FIELD_EMPTY
             result = false
         }
         if (firstName.isNotEmpty() && !Validation.isValidLetters(firstName)) {
             _enableFirstNameHelperText.value = SignUpConstants.FIELD_CONTAINS_SPECIAL_CHARACTERS
             result = false
         }
         if (firstName.isNotEmpty() && Validation.isValidLetters(firstName))
             _enableFirstNameHelperText.value = SignUpConstants.FIELD_CORRECT

         if (lastName.isEmpty()) {
             _enableLastNameHelperText.value = SignUpConstants.FIELD_EMPTY
             result = false
         }
         if (lastName.isNotEmpty() && !Validation.isValidLetters(lastName)) {
             _enableLastNameHelperText.value = SignUpConstants.FIELD_CONTAINS_SPECIAL_CHARACTERS
             result = false
         }
         if (lastName.isNotEmpty() && Validation.isValidLetters(lastName))
             _enableLastNameHelperText.value = SignUpConstants.FIELD_CORRECT

         if (address.isEmpty()) {
             _enableAddressHelperText.value = SignUpConstants.FIELD_EMPTY
             result = false
         }
         if (address.isNotEmpty() && !Validation.isValidAddressRange(address)) {
             _enableAddressHelperText.value = SignUpConstants.ADDRESS_NOT_IN_RANGE
             result = false
         }
         if (address.isNotEmpty() && !Validation.isValidAlphanumerics(address) && Validation.isValidAddressRange(address)) {
             _enableAddressHelperText.value = SignUpConstants.FIELD_CONTAINS_SPECIAL_CHARACTERS
             result = false
         }
         if (address.isNotEmpty() && Validation.isValidAlphanumerics(address) && Validation.isValidAddressRange(address))
             _enableAddressHelperText.value = SignUpConstants.FIELD_CORRECT

         if (dateOfBirth.isEmpty()) {
             _enableDateHelperText.value = SignUpConstants.FIELD_EMPTY
             result = false
         }
         if (dateOfBirth.isNotEmpty() && !Validation.isValidDate(dateOfBirth)) {
             _enableDateHelperText.value = SignUpConstants.FIELD_WRONG
             result = false
         }
         if (dateOfBirth.isNotEmpty() && Validation.isValidDate(dateOfBirth) && !Validation.isBeforeCurrentDate(dateOfBirth)) {
             _enableDateHelperText.value = SignUpConstants.DATE_CURRENT
             result = false
         }
         if (dateOfBirth.isNotEmpty() && Validation.isValidDate(dateOfBirth) && Validation.isBeforeCurrentDate(dateOfBirth))
             _enableDateHelperText.value = SignUpConstants.FIELD_CORRECT

         return result
    }

    private fun getUserData(
        email: String, firstName: String, lastName: String,
        address: String, dateOfBirth: String
    ) = hashMapOf(
        AppConstants.FIRESTORE_USER_EMAIL_FIELD to email,
        AppConstants.FIRESTORE_USER_FIRST_NAME_FIELD to firstName,
        AppConstants.FIRESTORE_USER_LAST_NAME_FIELD to lastName,
        AppConstants.FIRESTORE_USER_ADDRESS_FIELD to address,
        AppConstants.FIRESTORE_USER_DATE_OF_BIRTH_FIELD to dateOfBirth
    )


    fun signIn() {
        viewModelScope.launch {
            _navigateToSignInFragment.emit(true)
        }
    }
}