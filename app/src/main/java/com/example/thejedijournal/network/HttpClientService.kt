package com.example.thejedijournal.network

import com.example.thejedijournal.data.remote.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

/**
 * Http client for network calls
 */
class HttpClientService {

    val TAG = "HttpClientService"

    val client = HttpClient {

        // Content negotiation plugin for parsing JSON responses
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })

            expectSuccess = false
        }

        // Set timeouts to limit network call wait time.
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
            connectTimeoutMillis = 5000
            socketTimeoutMillis = 5000
        }

        // Expect Json response by default unless specified otherwise
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    suspend inline fun <reified T : Any> makeGETRequest(
        apiPath: String,
        queryParams: Map<String, String> = mapOf(),
    ): T {

        try {
            val responseModel: T = client.get(apiPath) {

                headers {
                    accept(ContentType.Application.Json)
                }

                queryParams.forEach {
                    parameter(it.key, it.value)
                }
            }.body()
            return responseModel
        } catch (e: ResponseException) {
            val errorContent = e.response.bodyAsText()

            // We may get error in parsing error content or the format may be different.
            // Use try-catch
            val errorModel = try {
                Json.decodeFromString(errorContent)
            } catch (e: Exception) {
                NetworkErrorModel(message = errorContent)
            }
            
            throw NetworkException(errorModel.copy(status = e.response.status.value))
        } catch (th: Throwable) {
            throw NetworkException(NetworkErrorModel(message = "Unknown error"))
        }
    }
}