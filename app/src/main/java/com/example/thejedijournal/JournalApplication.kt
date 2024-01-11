package com.example.thejedijournal

import android.app.Application
import android.content.*
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import com.example.thejedijournal.core.*
import com.example.thejedijournal.data.local.*
import com.example.thejedijournal.network.*

class JournalApplication: Application() {

    private lateinit var coreServices: CoreServices
    override fun onCreate() {
        super.onCreate()
        CoreContext.coreServices = CoreServices(
            scope = CoreCoroutineScope(),
            httpService = HttpClientService(),
            application = this
        )
        coreServices = CoreContext.coreServices
        coreServices.connectivityManager.registerDefaultNetworkCallback(coreServices.networkCallback)
    }

    override fun onTerminate() {
        super.onTerminate()
        coreServices.connectivityManager.unregisterNetworkCallback(coreServices.networkCallback)
    }
}