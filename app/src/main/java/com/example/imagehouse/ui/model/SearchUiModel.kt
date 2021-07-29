package com.example.imagehouse.ui.model

class SearchUiModel {
    val photos: MutableList<PhotoUiModel> = mutableListOf()
    var totalCount: Int? = null
}

data class PhotoUiModel(val url: String)