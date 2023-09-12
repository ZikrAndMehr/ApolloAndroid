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