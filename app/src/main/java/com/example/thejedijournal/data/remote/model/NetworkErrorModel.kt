package com.example.thejedijournal.data.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class NetworkErrorModel(@Transient val status: Int = 0, val message: String = "Unknown Error")