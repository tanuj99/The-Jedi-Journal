package com.example.thejedijournal.presentation.state

import com.example.thejedijournal.presentation.vm.*

data class CharacterListState(
    val characterListFetchState: FetchState = FetchState.NOT_REQUESTED,
    val characterFilmsFetchState: FetchState = FetchState.NOT_REQUESTED,
    val refreshCharacterList: Boolean = false,
    val refreshFilmsList: Boolean = false
): BaseState()
