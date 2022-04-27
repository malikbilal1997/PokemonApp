package com.thephoenixdevelopers.pokemonapp.di

import com.thephoenixdevelopers.pokemonapp.network.ApiInterface
import com.thephoenixdevelopers.pokemonapp.repos.PokemonRepo
import com.thephoenixdevelopers.pokemonapp.repos.PokemonRepoImpl
import com.thephoenixdevelopers.pokemonapp.utils.API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    // Provides Retrofit Client

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {

        return Retrofit.Builder().baseUrl(API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Provides Api Interface.

    @Singleton
    @Provides
    fun providesApiInterface(

        retrofit: Retrofit

    ): ApiInterface {

        return retrofit.create(ApiInterface::class.java)
    }

    // Provides Pokemon Repository.

    @Singleton
    @Provides
    fun providesPokemonRep(

        apiInterface: ApiInterface

    ): PokemonRepo = PokemonRepoImpl(apiInterface)

}