package com.thephoenixdevelopers.pokemonapp.ui.fragments.select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thephoenixdevelopers.pokemonapp.data.Pokemon
import com.thephoenixdevelopers.pokemonapp.data.PokemonSelect
import com.thephoenixdevelopers.pokemonapp.repos.PokemonRepo
import com.thephoenixdevelopers.pokemonapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SelectViewModel @Inject constructor(

    private val pokemonRepo: PokemonRepo

) : ViewModel() {

    private var _limit = 20
    private var _offset = 0

    // For Checking if the Network Call Running Already.
    private var _networkCallRunning: Boolean = false

    // For Checking if More Records Available on Server.
    private var _canFetchMoreRecords: Boolean = false

    private val _nothingFound = MutableStateFlow(false)
    val nothingFound = _nothingFound.asStateFlow()

    private val _pokemonProgress = MutableStateFlow(false)
    val pokemonProgress = _pokemonProgress.asStateFlow()

    // State Flow for Managing State of Pokemon List.
    private val _pokemonList = MutableStateFlow(listOf<PokemonSelect>())
    val pokemonList = _pokemonList.asStateFlow()

    init {

        fetchPokemonList()
    }

    fun fetchPokemonList() {

        if (!_networkCallRunning && !_canFetchMoreRecords) {

            // Setting Network Call Flag to True When Request Start.
            _networkCallRunning = true

            viewModelScope.launch {

                pokemonRepo.getPokemonList(

                    _limit, _offset

                ).collect { response ->

                    when (response) {

                        is Response.Idle -> {
                            _pokemonProgress.value = false
                        }

                        // If There's Some Error Occurred Hide Progress Bar.

                        is Response.Error -> {

                            Timber.d("Error -> %s", response.error)

                            _networkCallRunning = false

                            _nothingFound.value = _pokemonList.value.isEmpty()

                        }

                        is Response.Success -> {

                            Timber.d("Success -> Get Pokemon List Completed")

                            Timber.d("Pokemon List Size -> %s", response.success.size)

                            val pokemonList = mutableListOf<PokemonSelect>()

                            pokemonList.addAll(
                                _pokemonList.value.map { it.copy() }
                            )

                            pokemonList.addAll(
                                response.success.map {
                                    PokemonSelect(false, it.copy())
                                }
                            )

                            _canFetchMoreRecords = response.success.size < _limit

                            _pokemonList.value = pokemonList

                            _offset += _limit

                            _networkCallRunning = false

                            _pokemonProgress.value = false

                            _nothingFound.value = _pokemonList.value.isEmpty()
                        }

                        is Response.Loading -> {

                            _nothingFound.value = false
                            _pokemonProgress.value = true
                        }
                    }
                }
            }

        }

    }

    fun selectPokemon(pokemon: Pokemon) {

        val pokemonList = _pokemonList.value
            .toMutableList().map { it.copy() }

        pokemonList.filter {

            it.pokemon == pokemon

        }.forEach {

            it.selected = !it.selected
        }

        _pokemonList.value = pokemonList

    }

    fun getSelectedPokemon(): List<Pokemon> {

        val pokemonList = _pokemonList.value
            .toMutableList().map { it.copy() }

        val selectedList = pokemonList.filter { it.selected }

        return selectedList.toMutableList().map { it.pokemon }
    }
}