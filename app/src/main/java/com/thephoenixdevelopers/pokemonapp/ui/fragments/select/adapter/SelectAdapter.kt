package com.thephoenixdevelopers.pokemonapp.ui.fragments.select.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thephoenixdevelopers.pokemonapp.R
import com.thephoenixdevelopers.pokemonapp.data.PokemonSelect
import com.thephoenixdevelopers.pokemonapp.databinding.LayoutItemSelectBinding
import com.thephoenixdevelopers.pokemonapp.utils.PokemonImageUtil
import com.thephoenixdevelopers.pokemonapp.utils.PokemonSelectDiff
import java.util.*

class SelectAdapter(

    private val onItemClickListener: (PokemonSelect) -> Unit,

    ) : ListAdapter<PokemonSelect, SelectAdapter.SelectViewHolder>(PokemonSelectDiff) {

    inner class SelectViewHolder(

        private val binding: LayoutItemSelectBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.apply {

                setOnClickListener {
                    onItemClickListener(getItem(bindingAdapterPosition))
                }
            }

        }

        fun bind(pokemonSelect: PokemonSelect) {

            binding.apply {

                val url = PokemonImageUtil
                    .getImageUrl(pokemonSelect.pokemon.url)

                Glide.with(root).load(url).into(itemImage)

                if (pokemonSelect.selected) {

                    itemCheck.setImageResource(R.drawable.ic_check)
                } else {

                    itemCheck.setImageResource(R.drawable.ic_uncheck)
                }

                itemName.text = pokemonSelect.pokemon.name.replaceFirstChar {
                    if (it.isLowerCase()) {
                        it.titlecase(Locale.getDefault())
                    } else {
                        it.toString()
                    }
                }
            }

        }

    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {

        holder.bind(getItem(position))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {

        return SelectViewHolder(

            LayoutItemSelectBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    }
}