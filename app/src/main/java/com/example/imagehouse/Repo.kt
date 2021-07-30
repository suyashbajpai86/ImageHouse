package com.example.imagehouse

import android.util.Log
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class Repo {

    fun fetch(request: String): Result<String>? {
        val url = URL(request)
        Log.d("API_HIT", request)
        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val line = bufferedReader.readLine()
            return Result.Success(line)
        }
        return Result.Error(Exception("Cannot open HttpURLConnection"))
    }
}