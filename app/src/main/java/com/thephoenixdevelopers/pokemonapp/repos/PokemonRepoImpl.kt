package com.thephoenixdevelopers.pokemonapp.repos

import com.thephoenixdevelopers.pokemonapp.network.ApiInterface
import com.thephoenixdevelopers.pokemonapp.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PokemonRepoImpl(

    private val apiInterface: ApiInterface

) : PokemonRepo {

    override fun getPokemonList(

        limit: Int, offset: Int

    ) = flow {

        if (offset == 0) {
            emit(Response.Loading)
        }

        runCatching {

            apiInterface.getPokemonList(limit, offset)

        }.onSuccess {

            emit(Response.Success(it.results))

        }.onFailure {

            emit(Response.Error(it.message))
        }

    }.flowOn(Dispatchers.IO)


}