package com.thephoenixdevelopers.pokemonapp.ui.fragments.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle.State.RESUMED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thephoenixdevelopers.pokemonapp.databinding.FragmentSelectBinding
import com.thephoenixdevelopers.pokemonapp.ui.fragments.select.adapter.SelectAdapter
import com.thephoenixdevelopers.pokemonapp.utils.POKEMON_KEY
import com.thephoenixdevelopers.pokemonapp.utils.SELECT_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SelectFragment : Fragment() {

    private lateinit var selectAdapter: SelectAdapter

    private var _binding: FragmentSelectBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SelectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?
    ): View {

        _binding = FragmentSelectBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListAdapter()

        initClickListener()

        initPokemonObserver()

        initNothingObserver()

        initProgressObserver()

        initScrollPageListener()

    }

    private fun initListAdapter() {

        selectAdapter = SelectAdapter {
            viewModel.selectPokemon(it.pokemon)
        }

        binding.pokemonRecyclerView.apply {
            adapter = selectAdapter
        }

    }

    private fun initClickListener() {

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {

            val selectedPokemon = viewModel.getSelectedPokemon()

            val bundle = bundleOf(SELECT_KEY to selectedPokemon)

            setFragmentResult(POKEMON_KEY, bundle)

            findNavController().navigateUp()

        }

    }

    private fun initPokemonObserver() {

        lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(RESUMED) {

                viewModel.pokemonList.collect { response ->

                    selectAdapter.submitList(response)

                }
            }
        }

    }

    private fun initNothingObserver() {

        lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(RESUMED) {

                viewModel.nothingFound.collect {


                }
            }
        }

    }

    private fun initProgressObserver() {

        lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(RESUMED) {

                viewModel.pokemonProgress.collect { response ->

                    if (response) {

                        binding.progressBar.visibility = View.VISIBLE
                    } else {

                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

    }

    private fun initScrollPageListener() {

        val scrollListener = object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val manager = recyclerView.layoutManager as LinearLayoutManager

                val totalItemCount = manager.itemCount

                val visibleItemCount = manager.childCount

                val visibleItemPosition = manager
                    .findFirstVisibleItemPosition()

                if (visibleItemPosition + visibleItemCount == totalItemCount) {

                    Timber.d("Called -> Fetch Next Page of Pokemon")

                    // Fetch a next batch of records from the database

                    viewModel.fetchPokemonList()
                }
            }
        }

        binding.pokemonRecyclerView.addOnScrollListener(scrollListener)

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}