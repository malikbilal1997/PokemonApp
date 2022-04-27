package com.thephoenixdevelopers.pokemonapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.thephoenixdevelopers.pokemonapp.data.Pokemon

object PokemonDiff : DiffUtil.ItemCallback<Pokemon>() {

    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem == newItem
    }

}