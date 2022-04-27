package com.thephoenixdevelopers.pokemonapp.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.thephoenixdevelopers.pokemonapp.data.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    // State Flow for Managing State of Pokemon List.
    private val _pokemonList = MutableStateFlow(listOf<Pokemon>())
    val pokemonList = _pokemonList.asStateFlow()

    fun updateSelectedPokemon(pokemonList: List<Pokemon>) {

        _pokemonList.value = pokemonList
            .toMutableList().map { it.copy() }

    }

}