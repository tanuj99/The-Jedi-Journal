package com.example.thejedijournal.core

import android.content.*
import android.net.*
import android.util.*
import com.example.thejedijournal.*
import com.example.thejedijournal.data.local.*
import com.example.thejedijournal.data.remote.api.*
import com.example.thejedijournal.network.*

class CoreServices(
    val scope: CoreCoroutineScope,
    val httpService: HttpClientService,
    application: JournalApplication,
) {
    var apis = Api()
    val charactersDatabase = CharactersDatabase.create(application)
    val filmsDatabase = FilmsDatabase.create(application)

    var isDeviceOnline: Boolean = false

    val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            isDeviceOnline = true
            Log.d("CoreServices", "onAvailable: Device Online:")
        }

        override fun onLost(network: Network) {
            isDeviceOnline = false
            Log.d("CoreServices", "onLost: Device Offline")
        }
    }
}