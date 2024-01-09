package com.example.thejedijournal

import android.app.Application
import com.example.thejedijournal.core.*
import com.example.thejedijournal.network.*

class JournalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        CoreContext.coreServices = CoreServices(
            scope = CoreCoroutineScope(),
            httpService = HttpClientService()
        )
    }
}