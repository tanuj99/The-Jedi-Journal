package com.example.thejedijournal.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import com.example.thejedijournal.R
import com.example.thejedijournal.data.local.*
import com.example.thejedijournal.presentation.state.*
import com.example.thejedijournal.presentation.vm.*


@Composable
fun CharacterDetailScreen(
    navHostController: NavHostController,
    characterListVM: CharacterListVM
) {
    val character = characterListVM.selectedCharacter
    val characterListState = characterListVM.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(
                    id = R.drawable.background
                ),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = "BackArrow",
                    tint = Color.LightGray)
            }
            Text(
                text = "Character Details",
                fontSize = 25.sp,
                color = Color.Yellow,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(textAlign = TextAlign.Center), fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Name: ${character.name}",
                fontSize = 24.sp,
                color = Color.Yellow
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Films: ${character.films.size}",
                fontSize = 24.sp,
                color = Color.Yellow
            )
        }


        Text(
            text = "Film Catalogue",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp),
            color = Color.Yellow
        )

        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            when (characterListState.value.characterFilmsFetchState) {
                FetchState.REQUESTED -> {
                    items(6) {
                        CharacterShimmerView()
                    }
                }
                FetchState.SUCCESS -> {
                    items(characterListVM.characterFilmsList) { film ->
                        FilmRow(
                            films = film, modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }
                }
                FetchState.FAILURE  -> {
                    item {
                        Text(text = "Something went wrong, Please try again",
                            color = Color.Red,
                            modifier = Modifier.align(CenterHorizontally))
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun FilmRow(
    films: FilmsEntity,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.Yellow)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = films.title, color = Color.Yellow, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = films.releaseDate, color = Color.Yellow, fontSize = 18.sp)
                }
            }
        }
    }

}