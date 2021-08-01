package com.example.imagehouse.network

import com.example.imagehouse.ui.model.SearchUiModel

class Mapper {
    fun updateSearchUiModel(fromJson: SearchResponse?, uiModel: SearchUiModel, isNewSearch: Boolean) {
        if (isNewSearch) {
            uiModel.photos.clear()
        }
        uiModel.totalCount = fromJson?.photos?.total
        uiModel.errorMsg = "Total results - ${uiModel.totalCount}"
        fromJson?.photos?.photo?.forEach {
            uiModel.photos.add(it.toPhotoUiModel())
        }
    }

    fun clearSearchUiModel(uiModel: SearchUiModel) {
        uiModel.totalCount = 0
        uiModel.errorMsg = "No Results found."
    }
}