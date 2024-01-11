package com.example.thejedijournal.presentation.vm

import android.util.Log
import com.example.thejedijournal.core.*
import com.example.thejedijournal.data.local.*
import com.example.thejedijournal.data.mapper.*
import com.example.thejedijournal.data.remote.model.*
import com.example.thejedijournal.presentation.state.*
import kotlinx.coroutines.*

class CharacterListVM(private val coreServices: CoreServices): BaseVM<CharacterListState>() {


    private val TAG = "CharacterListVM"
    var charactersList = listOf<CharactersEntity>()
    private var nextPageUrl = ""

    var characterFilmsList = listOf<FilmsEntity>()
    var allFilmsList = listOf<FilmsEntity>()
    lateinit var selectedCharacter: CharactersEntity

    override fun initialState(): CharacterListState {
        return CharacterListState()
    }

    fun getInitialCharactersList() {
        mutableState.value = CharacterListState(characterListFetchState = FetchState.REQUESTED)
        val apiPath: String = coreServices.apis.PEOPLE_API
        coreServices.scope.launch {
            try {
                val response: RootPeopleResponse = coreServices.httpService.makeGETRequest(apiPath)
                charactersList = response.results.map { it.toCharactersEntity() }
                nextPageUrl = response.next
                mutableState.value = CharacterListState(characterListFetchState = FetchState.SUCCESS)
                clearDatabase()
                insertCharactersToDB(charactersList)
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

    // Clear Database when Fresh Character List is fetched
    private fun clearDatabase() {
        coreServices.scope.launch {
            coreServices.charactersDatabase.dao.clearCharactersList()
            coreServices.filmsDatabase.dao.clearFilmsList()
        }
    }

    fun loadMoreCharacters() {
        if (!mutableState.value.isFilterSortOperation) {
            mutableState.value = mutableState.value.copy(loadMoreCharacterListFetchState = FetchState.REQUESTED)
            coreServices.scope.launch {
                try {
                    val response: RootPeopleResponse = coreServices.httpService.makeGETRequest(nextPageUrl)
                    charactersList += response.results.map { it.toCharactersEntity() }
                    nextPageUrl = response.next
                    mutableState.value = mutableState.value.copy(loadMoreCharacterListFetchState = FetchState.SUCCESS)
                    insertCharactersToDB(charactersList)
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

    }

    fun getCharactersListFromDB() {
        coreServices.scope.launch {
            mutableState.value = mutableState.value.copy(characterListFetchState = FetchState.REQUESTED)
            charactersList = coreServices.charactersDatabase.dao.getCharacters()
            if (charactersList.isNotEmpty()) {
                mutableState.value = mutableState.value.copy(characterListFetchState = FetchState.SUCCESS)
            } else mutableState.value = mutableState.value.copy(characterListFetchState = FetchState.FAILURE)
        }
    }

    fun getFilmsListFromDB() {
        coreServices.scope.launch {
            mutableState.value =
                mutableState.value.copy(characterFilmsFetchState = FetchState.REQUESTED)
            allFilmsList = coreServices.filmsDatabase.dao.getFilms()
            if (allFilmsList.isNotEmpty()) {
                mutableState.value =
                    mutableState.value.copy(characterFilmsFetchState = FetchState.SUCCESS)
            } else mutableState.value =
                mutableState.value.copy(characterFilmsFetchState = FetchState.FAILURE)
        }
    }
    private suspend fun insertCharactersToDB(results: List<CharactersEntity>) {
        coreServices.charactersDatabase.dao.insertCharacterList(results)
    }

    private suspend fun insertFilmsToDB(results: List<FilmsEntity>) {
        coreServices.filmsDatabase.dao.insertFilmsList(results)
    }

    fun getFilmsForSelectedCharacter(character: CharactersEntity) {
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
                allFilmsList = response.results.map { it.toFilmsEntity() }
                insertFilmsToDB(allFilmsList)
                Log.d(TAG, "fetchAllFilms: ${response.results}")
            } catch (e: Exception) {
                mutableState.value = mutableState.value.copy(characterFilmsFetchState = FetchState.FAILURE)
                Log.e("Failure", "getAllFilms: Failed to fetch films list", e)
            }
        }
    }

    fun refreshCharacterList() {
        mutableState.value = mutableState.value.copy(refreshCharacterList = true)
        getInitialCharactersList()
    }

    fun sortCharactersListByName() {
        mutableState.value = mutableState.value.copy(isFilterSortOperation = true)
        charactersList = charactersList.sortedBy {
            it.name
        }
    }

    fun sortCharactersListByCreatedAt() {
        mutableState.value = mutableState.value.copy(isFilterSortOperation = true)
        charactersList = charactersList.sortedBy {
            it.created
        }
    }
    fun sortCharactersListByUpdatedAt() {
        mutableState.value = mutableState.value.copy(isFilterSortOperation = true)
        charactersList = charactersList.sortedBy {
            it.edited
        }
    }

    fun filterCharacterListByMale() {
        mutableState.value = mutableState.value.copy(isFilterSortOperation = true)
        charactersList = charactersList.filter {
            it.gender == "male"
        }
    }

    fun filterCharacterListByFemale() {
        mutableState.value = mutableState.value.copy(isFilterSortOperation = true)
        charactersList = charactersList.filter {
            it.gender == "female"
        }
    }
}