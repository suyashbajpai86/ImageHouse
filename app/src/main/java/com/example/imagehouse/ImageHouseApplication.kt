package com.example.imagehouse

import android.app.Application
import com.example.imagehouse.ui.model.BaseRVItem

class ImageHouseApplication: Application() {

    var favourites: MutableList<BaseRVItem> = mutableListOf()
}