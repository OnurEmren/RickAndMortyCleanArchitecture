package com.onuremren.breakingbadapp.network

import com.onuremren.breakingbadapp.model.Character
import com.onuremren.breakingbadapp.model.CharacterList
import retrofit2.Response

interface ApiHelper {
    suspend fun getCharacters(page: Int): Response<CharacterList>
    suspend fun getCharactersWithApi(page: Int): Response<com.onuremren.breakingbadapp.model.Character>
    suspend fun searchCharacterWithName(name: String): CharacterList
    suspend fun searchWithOtherThings(status: String, gender: String, page:Int): CharacterList
    suspend fun searchWithGender(gender: String,page: Int): CharacterList
    suspend fun searchWithStatus(status: String,page: Int): CharacterList

}