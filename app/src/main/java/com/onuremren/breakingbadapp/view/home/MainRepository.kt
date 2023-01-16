package com.onuremren.breakingbadapp.view.home

import com.onuremren.breakingbadapp.core.base.BaseRepository
import com.onuremren.breakingbadapp.core.util.NetworkHelper
import com.onuremren.breakingbadapp.di.DispatchersProvider
import com.onuremren.breakingbadapp.model.CharacterList
import com.onuremren.breakingbadapp.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    dispatchersProvider: DispatchersProvider,
    networkHelper: NetworkHelper,
) : BaseRepository(networkHelper, dispatchersProvider) {
    fun getCharactersFromApi(ricky: Int) = baseRequestFlow { apiService.getData(ricky) }
    fun getCharactersWithId(ricky: Int) = baseRequestFlow { apiService.getDataWithId(ricky) }

    suspend fun getCharactersByName(name: String): CharacterList{
        return apiService.searchCharactersByName(name)
    }

    suspend fun searchCharWithOtherThings(status: String, gender: String, page: Int) : CharacterList{
        return apiService.searchCharactersbyStatusAndGender(status, gender, page)

    }


    suspend fun getCharactersByStatus(status : String, page:Int): CharacterList{
        return apiService.getCharactersByStatus(status, page)
    }

    suspend fun getCharactersByGender(gender : String, page:Int): CharacterList {
        return apiService.getCharactersByGender(gender, page)


    }
}