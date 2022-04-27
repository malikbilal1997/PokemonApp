package com.thephoenixdevelopers.pokemonapp.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.thephoenixdevelopers.pokemonapp.R
import com.thephoenixdevelopers.pokemonapp.data.Pokemon
import com.thephoenixdevelopers.pokemonapp.databinding.FragmentHomeBinding
import com.thephoenixdevelopers.pokemonapp.ui.fragments.home.adapter.HomeAdapter
import com.thephoenixdevelopers.pokemonapp.utils.POKEMON_KEY
import com.thephoenixdevelopers.pokemonapp.utils.SELECT_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeAdapter: HomeAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?
    ): View {

        _binding = FragmentHomeBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListAdapter()

        initClickListener()

        initResultListener()

        initListSearchView()

        initPokemonObserver()

    }

    private fun initListAdapter() {

        homeAdapter = HomeAdapter {

        }

        binding.bottomSheetContainer.homeRecyclerView.apply {
            adapter = homeAdapter
        }

    }

    private fun initClickListener() {

        binding.gotoPokemonList.setOnClickListener {
            gotoSelectPokemonScreen()
        }

    }

    private fun initListSearchView() {

        binding.bottomSheetContainer.searchEdit.doOnTextChanged { text, _, _, _ ->

            text?.let { searchPokemon(it.toString()) } ?: searchPokemon(String())

        }


    }

    private fun initResultListener() {

        setFragmentResultListener(POKEMON_KEY) { _, bundle ->

            val result = bundle
                .getParcelableArrayList<Pokemon>(SELECT_KEY)

            Timber.d("Result Received -> %s", result)

            result?.let { viewModel.updateSelectedPokemon(it) }
        }

    }

    private fun initPokemonObserver() {

        lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(RESUMED) {

                viewModel.pokemonList.collect { response ->

                    homeAdapter.submitList(response)

                }
            }
        }

    }

    private fun gotoSelectPokemonScreen() {

        findNavController().navigate(R.id.action_homeFragment_to_selectFragment)

    }

    private fun searchPokemon(name: String) {

        val pokemonList = viewModel.pokemonList.value
            .toMutableList().map { pokemon -> pokemon.copy() }

        if (name.isEmpty()) {

            homeAdapter.submitList(pokemonList)

        } else {

            val filterList = pokemonList.filter {
                it.name.lowercase().contains(name.lowercase())
            }

            homeAdapter.submitList(filterList)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }
}