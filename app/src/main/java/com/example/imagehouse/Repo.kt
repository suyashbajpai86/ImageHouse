package com.example.imagehouse

import android.app.Application
import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class Repo(val application: Application) {

    fun fetch(request: String): Result<String>? {

        val sharedPreferences = application.getSharedPreferences("search",
            Context.MODE_PRIVATE
        )
        if (sharedPreferences.contains(request)){
            return Result.Success(sharedPreferences.getString(request, "")!!)
        } else {
            val url = URL(request)
            Log.d("API_HIT", request)
            (url.openConnection() as? HttpURLConnection)?.run {
                requestMethod = "GET"
                setRequestProperty("Content-Type", "application/json; utf-8")
                setRequestProperty("Accept", "application/json")
                try {
                    val inputStreamReader = InputStreamReader(inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    val line = bufferedReader.readLine()
                    val result =  Result.Success(line)
                    saveSearch(request, result)
                    return result
                }catch (e: java.lang.Exception){
                    Log.d("Exception", e.stackTraceToString())
                }
            }
            return Result.Error(Exception("Cannot open HttpURLConnection"))
        }
    }

    private fun saveSearch(
        text: CharSequence,
        fetch: Result.Success<String>
    ) {
        val sharedPreferences = application.getSharedPreferences("search",
            Context.MODE_PRIVATE
        )
        val edit = sharedPreferences.edit()
        edit.putString(text.toString(), fetch.data)
        edit.apply()
        val sharedPreferences1 = application.getSharedPreferences("searchTime",
            Context.MODE_PRIVATE
        )
        val edit1 = sharedPreferences1.edit()
        edit1.putString(text.toString(), System.currentTimeMillis().toString())
        edit1.apply()
    }
}