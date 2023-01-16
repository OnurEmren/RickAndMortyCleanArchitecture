package com.onuremren.breakingbadapp.view.detail

import com.onuremren.breakingbadapp.core.base.BaseViewModel
import com.onuremren.breakingbadapp.view.home.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : BaseViewModel<
        DetailViewModel.DetailViewState,
        DetailViewModel.DetailViewEffect,
        DetailViewModel.DetailViewEvent>() {




    init {
        viewState = DetailViewState()
    }

    override fun process(viewEvent: DetailViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is DetailViewEvent.GetCharDetail -> fetchDetail(viewEvent.id)
            is DetailViewEvent.ShowError -> navigateBackWithErrorMessage(viewEvent.message)
        }
    }

    private fun fetchDetail(id: Int) {
        makeApiCall(
            onFailure = {

            },

            onLoading = {
                viewState.copy(isLoading = true)

            },

            onSuccess = {
                viewState = viewState.copy(
                    charDetail = it,
                    isLoading = false
                )

            }
        ) {
            mainRepository.getCharactersWithId(id)

        }
    }


    private fun navigateBackWithErrorMessage(message: String) {
        viewEffect = DetailViewEffect.NavigateBack
    }

    sealed class DetailViewEvent {
        data class GetCharDetail(val id: Int) : DetailViewEvent()
        data class ShowError(val message: String) : DetailViewEvent()
    }

    data class DetailViewState(
        val charDetail: com.onuremren.breakingbadapp.model.Character? = null,
        val detailChar: com.onuremren.breakingbadapp.model.Character? = null,
        val isLoading: Boolean = false
    )

    sealed class DetailViewEffect {
        object NavigateBack : DetailViewEffect()
    }
}
