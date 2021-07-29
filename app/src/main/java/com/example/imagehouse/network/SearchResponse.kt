package com.example.imagehouse.network

import com.example.imagehouse.ui.model.PhotoUiModel
import com.google.gson.annotations.SerializedName

data class SearchResponse (
    @SerializedName("photos") val photos : Photos,
    @SerializedName("stat") val stat : String
)

data class Photos (
    @SerializedName("page") val page : Int,
    @SerializedName("pages") val pages : Int,
    @SerializedName("perpage") val perpage : Int,
    @SerializedName("total") val total : Int,
    @SerializedName("photo") val photo : List<Photo>
)

data class Photo (
    @SerializedName("id") val id : Long,
    @SerializedName("owner") val owner : String,
    @SerializedName("secret") val secret : String,
    @SerializedName("server") val server : Int,
    @SerializedName("farm") val farm : Int,
    @SerializedName("title") val title : String,
    @SerializedName("ispublic") val ispublic : Int,
    @SerializedName("isfriend") val isfriend : Int,
    @SerializedName("isfamily") val isfamily : Int
) {
    fun toPhotoUiModel(): PhotoUiModel {
        val url = "https://farm$farm.staticflickr.com/$server/${id}_${secret}_m.jpg"
        return PhotoUiModel(url)
    }
}