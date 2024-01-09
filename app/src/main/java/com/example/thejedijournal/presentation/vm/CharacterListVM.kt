package com.example.thejedijournal.presentation.vm

import com.example.thejedijournal.core.*
import com.example.thejedijournal.data.remote.api.*
import com.example.thejedijournal.data.remote.model.*
import com.example.thejedijournal.presentation.state.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class CharacterListVM(private val coreServices: CoreServices): BaseVM<CharacterListState>() {


    var charactersList = listOf<CharacterModel>()
    var nextPageUrl = ""
    var previousPageUrl: String? = ""
    var characterCount = 0

    override fun initialState(): CharacterListState {
        return CharacterListState()
    }

    fun getInitialCharactersList() {
        mutableState.value = CharacterListState(characterListFetchState = FetchState.REQUESTED)
        coreServices.scope.launch {
            try {
                val apiPath = coreServices.apis.PEOPLE_API
                val response: RootResponse = coreServices.httpService.makeGETRequest(apiPath)
                charactersList = response.results
                nextPageUrl = response.next
                previousPageUrl = response.previous
                characterCount = response.count
                mutableState.value = CharacterListState(characterListFetchState = FetchState.SUCCESS)
            } catch (e: Exception) {
                if (charactersList.isEmpty()) {
                 mutableState.value = CharacterListState(
                     characterListFetchState = FetchState.FAILURE
                 )
                }
            }
        }
    }
}