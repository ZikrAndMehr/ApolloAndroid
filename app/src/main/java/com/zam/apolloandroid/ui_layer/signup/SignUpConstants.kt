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

object SignUpConstants {
    const val FIELD_CORRECT = "CORRECT"
    const val FIELD_EMPTY = "EMPTY"
    const val FIELD_WRONG = "WRONG"
    const val FIELD_CONTAINS_SPECIAL_CHARACTERS = "SPECIAL"

    const val PASSWORD_SHORT = "SHORT"
    const val PASSWORD_RANGE = 8
    const val ADDRESS_RANGE_MIN = 5
    const val ADDRESS_RANGE_MAX = 150
    const val ADDRESS_NOT_IN_RANGE = "NOT IN RANGE"
    const val DATE_CURRENT = "CURRENT"
}