package com.thephoenixdevelopers.pokemonapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.thephoenixdevelopers.pokemonapp.data.Pokemon
import com.thephoenixdevelopers.pokemonapp.data.PokemonSelect

object PokemonSelectDiff : DiffUtil.ItemCallback<PokemonSelect>() {

    override fun areItemsTheSame(oldItem: PokemonSelect, newItem: PokemonSelect): Boolean {
        return oldItem.pokemon.name == newItem.pokemon.name
    }

    override fun areContentsTheSame(oldItem: PokemonSelect, newItem: PokemonSelect): Boolean {
        return oldItem == newItem
    }

}