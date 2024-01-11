package com.example.thejedijournal.presentation.vm

import android.util.Log
import com.example.thejedijournal.core.*
import com.example.thejedijournal.data.remote.model.*
import com.example.thejedijournal.presentation.state.*
import kotlinx.coroutines.*

class CharacterListVM(private val coreServices: CoreServices): BaseVM<CharacterListState>() {


    private val TAG = "CharacterListVM"
    var charactersList = listOf<CharacterModel>()
    var nextPageUrl = ""
    var previousPageUrl: String? = ""
    private var currentPageUrl = ""

    var characterFilmsList = listOf<FilmsModel>()
    var allFilmsList = listOf<FilmsModel>()
    lateinit var selectedCharacter: CharacterModel

    override fun initialState(): CharacterListState {
        return CharacterListState()
    }

    fun getCharactersList(apiPath: String = coreServices.apis.PEOPLE_API) {
        mutableState.value = CharacterListState(characterListFetchState = FetchState.REQUESTED)
        currentPageUrl = apiPath
        coreServices.scope.launch {
            try {
                val response: RootPeopleResponse = coreServices.httpService.makeGETRequest(apiPath)
                charactersList = response.results
                nextPageUrl = response.next
                previousPageUrl = response.previous
                mutableState.value = CharacterListState(characterListFetchState = FetchState.SUCCESS)
            } catch (e: Exception) {
                if (charactersList.isEmpty()) {
                    Log.e(TAG, "Failed to fetch characters", e)
                 mutableState.value = CharacterListState(
                     characterListFetchState = FetchState.FAILURE
                 )
                }
            }
        }
    }

    fun getFilmsForSelectedCharacter(character: CharacterModel) {
        mutableState.value = mutableState.value.copy(characterFilmsFetchState = FetchState.REQUESTED)
        selectedCharacter = character

        if (allFilmsList.isEmpty()) {
            fetchAllFilms()
        }
        characterFilmsList = allFilmsList.filter { film ->
            character.films.contains(film.url)
        }
        Log.d(TAG, "getFilmsForSelectedCharacter: $characterFilmsList")
        mutableState.value = mutableState.value.copy(characterFilmsFetchState = FetchState.SUCCESS)
        
    }

    fun fetchAllFilms() {
        coreServices.scope.launch {
            try {
                val apiPath = coreServices.apis.FILM_API
                val response: RootFilmsResponse = coreServices.httpService.makeGETRequest(apiPath)
                Log.d(TAG, "fetchAllFilms: $response")
                allFilmsList = response.results
                Log.d(TAG, "fetchAllFilms: ${response.results}")
            } catch (e: Exception) {
                mutableState.value = mutableState.value.copy(characterFilmsFetchState = FetchState.FAILURE)
                Log.e("Failure", "getAllFilms: Failed to fetch films list", e)
            }
        }
    }

    fun refreshCharacterList() {
        mutableState.value = mutableState.value.copy(refreshCharacterList = true)
        getCharactersList(currentPageUrl)
    }

    fun sortCharactersListByName() {
        charactersList = charactersList.sortedBy {
            it.name
        }
    }

    fun sortCharactersListByCreatedAt() {
        charactersList = charactersList.sortedBy {
            it.created
        }
    }
    fun sortCharactersListByUpdatedAt() {
        charactersList = charactersList.sortedBy {
            it.edited
        }
    }

    fun filterCharacterListByMale() {
        charactersList = charactersList.filter {
            it.gender == "male"
        }
    }

    fun filterCharacterListByFemale() {
        charactersList = charactersList.filter {
            it.gender == "female"
        }
    }
}