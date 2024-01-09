package com.example.thejedijournal.data.remote.api

class ApiConstants {

    companion object {
        const val pageParam = "page"
        const val searchParam = "search"

    }
}

class Api {
    private val BASE_URL = "https://swapi.dev/api/"
    private val peopleResource = "people"
    private val filmsResource = "films"
    private val starshipsResource = "starships"
    private val vehiclesResource = "vehicles"
    private val planetsResource = "planets"
    private val speciesResource = "species"

    val PEOPLE_API = BASE_URL + peopleResource
    val FILM_API = BASE_URL + filmsResource
    val STARSHIP_API = BASE_URL + starshipsResource
    val VEHICLE_API = BASE_URL + vehiclesResource
    val PLANET_API = BASE_URL + planetsResource
    val SPECIES_API = BASE_URL + speciesResource
}