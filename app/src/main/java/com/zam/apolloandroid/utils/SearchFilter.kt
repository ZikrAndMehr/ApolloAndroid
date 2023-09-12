package com.zam.apolloandroid.utils

import android.widget.Filter
import com.zam.apolloandroid.model.Country

abstract class SearchFilter : Filter() {
    abstract fun searchFilter(constraint: CharSequence?, currentList: List<Country>)
}