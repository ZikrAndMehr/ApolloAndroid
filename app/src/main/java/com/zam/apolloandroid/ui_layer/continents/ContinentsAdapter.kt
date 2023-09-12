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

package com.zam.apolloandroid.ui_layer.continents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zam.apolloandroid.model.Continent
import com.zam.apolloandroid.R
import com.zam.apolloandroid.databinding.ContinentHolderBinding
import com.zam.apolloandroid.utils.ApolloContinent

class ContinentsAdapter(
    private val startCountriesFragmentFunction: (code: String) -> Unit
) : ListAdapter<Continent, ContinentsAdapter.ContinentHolder>(ContinentDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContinentHolder {
        val binding = ContinentHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContinentHolder(binding)
    }

    override fun onBindViewHolder(holder: ContinentHolder, position: Int) {
        val myContinent = getItem(position)
        holder.bind(myContinent.apolloContinent.code, myContinent.apolloContinent, myContinent.percentage)
    }

    inner class ContinentHolder(
        private val binding: ContinentHolderBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private lateinit var code: String

        fun bind(code: String, apolloContinent: ApolloContinent, percentage: Double) {
            this.code = code
            binding.apolloContinent = apolloContinent
            binding.tvStarCount.text = String.format(binding.root.context.getString(R.string.pct), percentage)
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) { startCountriesFragmentFunction(code) }
    }

    class ContinentDiffItemCallback : DiffUtil.ItemCallback<Continent>() {
        override fun areItemsTheSame(oldItem: Continent, newItem: Continent) = oldItem.apolloContinent.code == newItem.apolloContinent.code

        override fun areContentsTheSame(oldItem: Continent, newItem: Continent) = oldItem == newItem
    }
}