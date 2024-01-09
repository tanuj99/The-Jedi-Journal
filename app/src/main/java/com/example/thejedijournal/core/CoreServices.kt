package com.example.thejedijournal.core

import com.example.thejedijournal.data.remote.api.*
import com.example.thejedijournal.network.*

class CoreServices(val scope: CoreCoroutineScope,
                   val httpService: HttpClientService) {
    var apis = Api()
}