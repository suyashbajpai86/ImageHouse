package com.example.imagehouse.ui.home

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.imagehouse.ImageHouseApplication
import com.example.imagehouse.Repo
import com.example.imagehouse.Result
import com.example.imagehouse.network.Mapper
import com.example.imagehouse.network.SearchResponse
import com.example.imagehouse.ui.model.SearchUiModel
import com.example.imagehouse.ui.model.TextUiModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.UnsupportedEncodingException
import java.net.URL
import java.net.URLDecoder
import java.util.*
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.set
import kotlin.collections.sortedBy
import kotlin.collections.toList
import kotlin.collections.toTypedArray


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var page = 1
    val repo = Repo(application)
    val mapper = Mapper()
    val updateUi: MutableLiveData<SearchUiModel> = MutableLiveData()
    val _init: SearchUiModel = SearchUiModel()

    fun searchImages(text: CharSequence?, isNewSearch: Boolean) {
        if (!text.isNullOrEmpty()) {
            viewModelScope.launch(Dispatchers.IO){
                val request = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ccc929eca8aa542fb8bd1aabc445ca53&tags=$text&format=json&nojsoncallback=1&per_page=10&page=$page"
                when (val fetch = repo.fetch(request)) {
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

    fun showHistory() {
        val sharedPreferences = getApplication<ImageHouseApplication>().getSharedPreferences("searchTime", MODE_PRIVATE)
        val sortedBy = sharedPreferences.all.toList().sortedBy {
            (it.second as? String)?.toLong()
        }
        _init.apply {
            totalCount = sortedBy.size
            msg = "History"
            photos.clear()
            var prevTag = ""
            sortedBy.forEachIndexed { index, pair ->
                val map = splitQuery(URL(pair.first))
                map?.get("tags")?.let { it1 ->
                    if(it1 != prevTag) {
                        photos.add(TextUiModel(it1))
                    }
                    prevTag = it1
                }
            }
        }
        updateUi.postValue(_init)
    }

    @Throws(UnsupportedEncodingException::class)
    fun splitQuery(url: URL): Map<String, String>? {
        val queryPairs: MutableMap<String, String> = LinkedHashMap()
        val query = url.query
        val pairs = query.split("&").toTypedArray()
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            queryPairs[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] =
                URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
        }
        return queryPairs
    }

    private val _text = MutableLiveData<String>().apply {
        value = "Image House"
    }
    val text: LiveData<String> = _text
}