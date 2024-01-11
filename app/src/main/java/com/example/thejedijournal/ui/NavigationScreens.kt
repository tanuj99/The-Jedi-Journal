package com.example.thejedijournal.ui

sealed class NavigationScreens(val route: String) {
    object CharacterListScreen: NavigationScreens("characterListScreen")

    object CharacterDetailScreen: NavigationScreens("characterDetailScreen")
}