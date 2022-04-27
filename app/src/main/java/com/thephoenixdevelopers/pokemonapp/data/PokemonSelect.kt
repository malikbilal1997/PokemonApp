package com.thephoenixdevelopers.pokemonapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonSelect(

    var selected: Boolean = false,
    var pokemon: Pokemon = Pokemon()

) : Parcelable