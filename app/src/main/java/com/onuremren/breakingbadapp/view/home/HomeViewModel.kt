package com.onuremren.breakingbadapp.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.onuremren.breakingbadapp.core.base.BaseViewModel
import com.onuremren.breakingbadapp.core.util.exhaustive
import com.onuremren.breakingbadapp.di.DispatchersProvider
import com.onuremren.breakingbadapp.model.Character
import com.onuremren.breakingbadapp.model.CharacterList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository
) :
    BaseViewModel<HomeViewModel.HomeViewState, HomeViewModel.HomeViewEffect, HomeViewModel.HomeViewEvent>() {

    companion object {
        const val CHARACTERS = -1

    }

    var filterValue = MutableLiveData<Array<Int>>()
    var isFilter = MutableLiveData<Boolean>()
    var listCharactersInEpisode = MutableLiveData<List<com.onuremren.breakingbadapp.model.Character>>()

    private var _currentPage = MutableStateFlow(CHARACTERS)
    val currentPage: StateFlow<Int> = _currentPage

    init {
        viewState = HomeViewState(

            isLoadingCharacters = true,
            isPagingLoading = false
        )
        refreshAll()

    }

    fun refreshAll() {
        fetchChar()
    }

    fun getByName(name: String){
        viewModelScope.launch{
            val characters = mainRepository.getCharactersByName(name)
            listCharactersInEpisode.value = characters.results
            isFilter.value = true
        }
    }

    fun getByStatusAndGender(status : String, gender: String, page:Int){
        viewModelScope.launch{
            val characters = mainRepository.searchCharWithOtherThings(status, gender, page)
            listCharactersInEpisode.value = characters.results
            isFilter.value = true
        }
    }

    fun getByStatus(status : String, page:Int){
        viewModelScope.launch(Dispatchers.IO){
            val characters = mainRepository.getCharactersByStatus(status,page)
            listCharactersInEpisode.value = characters.results
            isFilter.value = true
        }
    }

    fun getByGender(gender: String, page:Int){
        viewModelScope.launch(Dispatchers.IO){
            val characters = mainRepository.getCharactersByGender(gender, page)
            listCharactersInEpisode.value = characters.results
            isFilter.value = true
        }
    }

    private fun fetchChar(page: Int = 1) {
        makeApiCall(
            onFailure = {
                viewState = viewState.copy(isLoadingCharacters = false)
            },
            onSuccess = {
                viewState = viewState.copy(
                    charactersList = it?.results
                )

            },
            onLoading = {
                viewState = viewState.copy(isLoadingCharacters = true)
            },
        ) { mainRepository.getCharactersFromApi(1) }
    }

    private fun observeState() {
        viewState = viewState.copy()
    }


    private fun itemClicked(charId: com.onuremren.breakingbadapp.model.Character) {
        viewEffect = HomeViewEffect.GoToDetailPage(charId.id)
    }


    override fun process(viewEvent: HomeViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is HomeViewEvent.GetAllChar -> fetchChar()
            is HomeViewEvent.ClickToItem -> itemClicked(viewEvent.item)
            is HomeViewEvent.IdleState -> observeState()
            is HomeViewEvent.RefreshAll -> refreshAll()

        }.exhaustive
    }

    sealed class HomeViewEvent {
        object IdleState : HomeViewEvent()
        object RefreshAll : HomeViewEvent()
        object GetAllChar : HomeViewEvent()
        data class ClickToItem(val item: Character) : HomeViewEvent()
    }

    data class HomeViewState(
        val charactersList: List<Character>? = emptyList(),
        val isLoadingCharacters: Boolean = false,
        val isPagingLoading: Boolean = false,

        )

    sealed class HomeViewEffect {
        data class GoToDetailPage(val charId: Int) : HomeViewEffect()
    }


}