package com.onuremren.breakingbadapp.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character (
    //SerializedName Gson'daki verilerle modelimizdeki verilerin eşleşmesini sağlıyor.
    //isim-value -> aynı olmalı.
    var id : Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("status")
    var status : String,
    @SerializedName("species")
    var species: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("origin")
    var origin : Location,
    @SerializedName("location")
    var location : Location,
    @SerializedName("image")
    var image : String,
    @SerializedName("episode")
    var episode : List<String>
): Parcelable