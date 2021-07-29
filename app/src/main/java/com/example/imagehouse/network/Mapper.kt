package com.example.imagehouse.network

import com.example.imagehouse.ui.model.SearchUiModel

class Mapper {
    fun updateSearchUiModel(fromJson: SearchResponse?, uiModel: SearchUiModel) {
        uiModel.photos.clear()
        uiModel.totalCount = fromJson?.photos?.total
        fromJson?.photos?.photo?.forEach {
            uiModel.photos.add(it.toPhotoUiModel())
        }
    }
}