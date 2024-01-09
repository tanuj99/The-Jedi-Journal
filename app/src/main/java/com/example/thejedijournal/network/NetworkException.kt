package com.example.thejedijournal.network

import com.example.thejedijournal.data.remote.model.*

/**
 * General exception to be thrown from network layer
 */
data class NetworkException(val error: NetworkErrorModel): Exception()