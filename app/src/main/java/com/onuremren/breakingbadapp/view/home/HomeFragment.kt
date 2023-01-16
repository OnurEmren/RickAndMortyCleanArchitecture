package com.onuremren.breakingbadapp.view.home

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isNotEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.onuremren.breakingbadapp.MainActivity
import com.onuremren.breakingbadapp.R
import com.onuremren.breakingbadapp.core.base.BaseFragment
import com.onuremren.breakingbadapp.core.util.exhaustive
import com.onuremren.breakingbadapp.databinding.FragmentHomeBinding
import com.onuremren.breakingbadapp.model.Location
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding,
        HomeViewModel.HomeViewState,
        HomeViewModel.HomeViewEffect,
        HomeViewModel.HomeViewEvent,
        HomeViewModel>(FragmentHomeBinding::inflate) {

    private lateinit var homeCharactersAdapter: HomeCharactersAdapter
    private lateinit var mainActivity: MainActivity




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)




        viewModel.listCharactersInEpisode.observe(viewLifecycleOwner) {
            homeCharactersAdapter.submitList(it)
        }


        getNameSearchView()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search_char) {
            binding.searchView.animate().apply {
                duration = 700
                rotationXBy(360f)

            }.start()
                    if (binding.searchView.visibility == View.GONE) {

                        binding.searchView.visibility = View.VISIBLE
                       // binding.txtReset.visibility = View.VISIBLE


                        } else {
                        binding.searchView.visibility = View.GONE
                       // binding.txtReset.visibility = View.GONE
                        binding.searchView.animate().apply {
                            duration = 500
                            rotationXBy(-360f)

                        }.start()
                    }

                }
        binding.searchView.clearAnimation()
        return true

    }
    private fun getNameSearchView(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getByName(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getByName(newText.toString())
                return true
            }
        })
    }


    private fun bindViewState(viewState: HomeViewModel.HomeViewState) {
        binding.apply {
            this.listChar = viewState
        }
    }


    override val viewModel: HomeViewModel by viewModels()

    override fun init() {
        setupAdapter()
        viewModel.process(HomeViewModel.HomeViewEvent.IdleState)

    }

    override fun renderViewState(viewState: HomeViewModel.HomeViewState) {
        bindViewState(viewState)

    }


    private fun setupAdapter() {
        homeCharactersAdapter = HomeCharactersAdapter {
            viewModel.process(HomeViewModel.HomeViewEvent.ClickToItem(it))

        }


        binding.apply {
            recyclerview.adapter = homeCharactersAdapter


        }
    }


    private fun navigateToDetail(char:com.onuremren.breakingbadapp.model.Character, origin: Location, charId: Int) {
        val directions = HomeFragmentDirections.actionHomeFragmentToDetailFragment(char,origin,charId)
        navigateTo(directions)
    }


    override fun renderViewEffect(viewEffect: HomeViewModel.HomeViewEffect) {
        when (viewEffect) {
            is HomeViewModel.HomeViewEffect.GoToDetailPage -> navigateToDetail(viewEffect.char,viewEffect.origin,viewEffect.charId)
        }.exhaustive
    }


}