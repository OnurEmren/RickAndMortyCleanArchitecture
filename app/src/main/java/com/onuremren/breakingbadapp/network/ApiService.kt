package com.onuremren.breakingbadapp.network

import com.onuremren.breakingbadapp.core.util.Constants.ENDPOINT
import com.onuremren.breakingbadapp.model.Character
import com.onuremren.breakingbadapp.model.CharacterList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/character")
    suspend fun getData(
        @Query("ricky") ricky : Int
    ): Response<CharacterList>

    @GET("api/character")
    suspend fun getDataWithId(
        @Query("ricky") ricky : Int
    ): Response<com.onuremren.breakingbadapp.model.Character>

    @GET("api/character")
    suspend fun searchCharactersByName(@Query("name") name : String): CharacterList

    @GET("api/character")
    suspend fun searchCharactersbyStatusAndGender(@Query("status") status : String,
                                               @Query("gender") gender : String,
                                               @Query("page") page : Int): CharacterList

    @GET("api/character")
    suspend fun getCharactersByStatus(@Query("status") status : String, @Query("page") page : Int): CharacterList

    @GET("api/character")
    suspend fun getCharactersByGender( @Query("gender") gender : String, @Query("page") page : Int): CharacterList

}