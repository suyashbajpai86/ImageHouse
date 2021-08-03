package com.example.imagehouse.ui.model

class SearchUiModel {
    var msg = "No results found"
    val photos: MutableList<BaseRVItem> = mutableListOf()
    var totalCount: Int? = null
}

data class PhotoUiModel(val url: String): BaseRVItem()

data class TextUiModel(val text: String): BaseRVItem()

open class BaseRVItem
