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

package com.zam.apolloandroid.ui_layer.countries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zam.apolloandroid.R
import com.zam.apolloandroid.databinding.CountryHolderBinding
import com.zam.apolloandroid.model.Country
import com.zam.apolloandroid.utils.SearchFilter

class CountriesAdapter(
    private val starCountryFunction: (countryName: String) -> Unit,
    private val unStarCountryFunction: (countryName: String) -> Unit
) : ListAdapter<Country, CountriesAdapter.CountryHolder>(CountryDiffItemCallback()), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val binding = CountryHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        val country = getItem(position)
        holder.bind(country)
    }

    override fun getFilter() = object : SearchFilter() {
        var countriesList = listOf<Country>()

        override fun searchFilter(constraint: CharSequence?, currentList: List<Country>) {
            super.filter(constraint)
            countriesList = currentList
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = if (constraint.isNullOrEmpty()) {
                countriesList
            } else {
                countriesList.filterIndexed { _, country: Country ->
                    country.name.lowercase().contains(constraint.toString().lowercase().trim())
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as List<Country>?)
        }
    }

    inner class CountryHolder(
        private val binding: CountryHolderBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var countryName: String
        private var countryStarred = false

        fun bind(country: Country) {
            binding.country = country

            countryName = country.name
            countryStarred = country.starred

            if (countryStarred) setStarImage()
            else setUnStarImage()

            binding.ivStar.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (countryStarred) unStarCountryFunction(countryName)
            else starCountryFunction(countryName)
        }

        private fun setStarImage() { binding.ivStar.setImageResource(R.drawable.ic_star) }

        private fun setUnStarImage() { binding.ivStar.setImageResource(R.drawable.ic_star_border) }
    }

    class CountryDiffItemCallback : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country) = oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Country, newItem: Country) = oldItem == newItem
    }
}