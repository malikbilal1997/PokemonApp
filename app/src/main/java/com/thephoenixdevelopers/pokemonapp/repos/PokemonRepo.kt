package com.thephoenixdevelopers.pokemonapp.repos

import com.thephoenixdevelopers.pokemonapp.data.Pokemon
import com.thephoenixdevelopers.pokemonapp.utils.Response
import kotlinx.coroutines.flow.Flow


interface PokemonRepo {

    fun getPokemonList(limit: Int, offset: Int): Flow<Response<List<Pokemon>>>

}