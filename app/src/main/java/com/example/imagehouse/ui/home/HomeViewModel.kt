package com.example.imagehouse.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagehouse.Repo
import com.example.imagehouse.Result
import com.example.imagehouse.network.Mapper
import com.example.imagehouse.network.SearchResponse
import com.example.imagehouse.ui.model.SearchUiModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var page = 1
    val repo = Repo()
    val mapper = Mapper()
    val updateUi: MutableLiveData<SearchUiModel> = MutableLiveData()
    val _init: SearchUiModel = SearchUiModel()

    fun searchImages(text: CharSequence?, isNewSearch: Boolean) {
        if (!text.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val request = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ccc929eca8aa542fb8bd1aabc445ca53&tags=$text&format=json&nojsoncallback=1&per_page=10&page=$page"
                val fetch = repo.fetch(request)
                when (fetch) {
                    is Result.Success<String> -> {
                        val fromJson = Gson().fromJson(fetch.data, SearchResponse::class.java)
                        mapper.updateSearchUiModel(fromJson, _init, isNewSearch)
                        updateUi.postValue(_init)
                    }
                    else -> {
                        mapper.clearSearchUiModel(_init)
                        updateUi.postValue(_init)
                    }
                }
            }
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "Image House"
    }
    val text: LiveData<String> = _text
}