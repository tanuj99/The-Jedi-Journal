package com.example.thejedijournal.presentation.state

import com.example.thejedijournal.presentation.vm.*

data class CharacterListState(
    val characterListFetchState: FetchState = FetchState.NOT_REQUESTED,
    val loadNextCharacterListFetchState: FetchState = FetchState.NOT_REQUESTED,
): BaseState() {
    companion object {
        fun default(): CharacterListState {
            return CharacterListState()
        }
    }
}

data class CharacterListSortOrderState(
    val characterListSortOrderFetchState: FetchState = FetchState.NOT_REQUESTED,
): BaseState() {
    companion object {
        fun default(): CharacterListState {
            return CharacterListState()
        }
    }
}
