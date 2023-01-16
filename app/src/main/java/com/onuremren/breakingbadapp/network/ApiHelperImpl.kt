package com.onuremren.breakingbadapp.network

import com.onuremren.breakingbadapp.model.Character
import com.onuremren.breakingbadapp.model.CharacterList
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getCharacters(page: Int): Response<CharacterList> {
        return apiService.getData(page)
    }

    override suspend fun getCharactersWithApi(page: Int): Response<Character> {
        return apiService.getDataWithId(page)
    }

    override suspend fun searchCharacterWithName(name: String): CharacterList {
        return apiService.searchCharactersByName(name)
    }

    override suspend fun searchWithOtherThings(
        status: String,
        gender: String,
        page: Int
    ): CharacterList {
        return apiService.searchCharactersbyStatusAndGender(status,gender, page)
    }

    override suspend fun searchWithGender(gender: String, page: Int): CharacterList {
        return apiService.getCharactersByGender(gender,page)
    }

    override suspend fun searchWithStatus(status: String, page: Int): CharacterList {
        return apiService.getCharactersByStatus(status, page)
    }


}