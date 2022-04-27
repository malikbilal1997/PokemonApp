package com.thephoenixdevelopers.pokemonapp.ui.fragments.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thephoenixdevelopers.pokemonapp.data.Pokemon
import com.thephoenixdevelopers.pokemonapp.databinding.LayoutItemHomeBinding
import com.thephoenixdevelopers.pokemonapp.utils.PokemonDiff
import com.thephoenixdevelopers.pokemonapp.utils.PokemonImageUtil
import java.util.*

class HomeAdapter(

    private val onItemClickListener: (Pokemon) -> Unit,

    ) : ListAdapter<Pokemon, HomeAdapter.HomeViewHolder>(PokemonDiff) {

    inner class HomeViewHolder(

        private val binding: LayoutItemHomeBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.apply {

                setOnClickListener {
                    onItemClickListener(getItem(bindingAdapterPosition))
                }
            }

        }

        fun bind(pokemon: Pokemon) {

            binding.apply {

                val url = PokemonImageUtil
                    .getImageUrl(pokemon.url)

                Glide.with(root).load(url).into(itemImage)

                itemName.text = pokemon.name.replaceFirstChar {
                    if (it.isLowerCase()) {
                        it.titlecase(Locale.getDefault())
                    } else {
                        it.toString()
                    }
                }
            }

        }

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        holder.bind(getItem(position))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        return HomeViewHolder(

            LayoutItemHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        )

    }
}