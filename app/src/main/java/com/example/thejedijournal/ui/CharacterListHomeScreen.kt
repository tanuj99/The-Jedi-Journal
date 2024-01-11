package com.example.thejedijournal.ui

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.thejedijournal.R
import com.example.thejedijournal.core.*
import com.example.thejedijournal.presentation.state.*
import com.example.thejedijournal.presentation.vm.*
import com.example.thejedijournal.ui.theme.*
import com.google.accompanist.swiperefresh.*

class CharacterListHomeScreen : ComponentActivity() {

    private val coreServices = CoreContext.coreServices
    private var characterListVM = CharacterListVM(coreServices = coreServices)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            TheJediJournalTheme {
                NavHost(navController = navHostController, startDestination = NavigationScreens.CharacterListScreen.route) {
                    composable(NavigationScreens.CharacterListScreen.route) {
                        HomeScreenBase(navHostController)
                    }
                    composable(NavigationScreens.CharacterDetailScreen.route) {
                        CharacterDetailScreen(navHostController, characterListVM = characterListVM)
                    }
                }
            }
        }
        characterListVM.getCharactersList()
        characterListVM.fetchAllFilms()
    }

    @Composable
    private fun HomeScreenBase(navHostController: NavHostController) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            HomeScreen(navHostController)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(navHostController: NavHostController) {

        val characterListState = characterListVM.state.collectAsState()
        val sheetState = rememberModalBottomSheetState()
        val showFilterBottomSheet = remember { mutableStateOf(false) }
        val showSortBottomSheet = remember { mutableStateOf(false) }
        val showSettingOptions = remember { mutableStateOf(false) }

        val swipeRefreshState = rememberSwipeRefreshState(
            isRefreshing = characterListState.value.refreshCharacterList
        )

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
            Spacer(modifier = Modifier
                .height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround) {
                Spacer(modifier = Modifier.weight(0.9f))
                Image(
                    painter = painterResource(
                        id = R.drawable.star_wars
                    ),
                    contentDescription = "Star wars logo",
                    modifier = Modifier
                        .fillMaxHeight(0.13f)
                        .padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.weight(0.5f))

                Column() {
                    IconButton(onClick = { showSettingOptions.value = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Settings", tint = Color.Yellow)
                    }
                    DropdownMenu(
                        expanded = showSettingOptions.value,
                        onDismissRequest = { showSettingOptions.value = false }
                    ) {
                        Column(modifier = Modifier.background(Color.Black)) {
                            TextButton(
                                onClick = {
                                        showFilterBottomSheet.value = true
                                },
                            ) {
                                Icon(imageVector = Icons.Filled.List, contentDescription = "Filter", tint = Color.LightGray)
                                Text(text = "Filter", color = Color.LightGray, modifier = Modifier.padding(horizontal = 4.dp))
                            }

                            TextButton(
                                onClick = {
                                    showSortBottomSheet.value = true
                                },
                            ) {
                                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Sort", tint = Color.LightGray)
                                Text(text = "Sort", color = Color.LightGray, modifier = Modifier.padding(horizontal = 4.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier
                .height(24.dp))

            SwipeRefresh(state = swipeRefreshState, onRefresh = {
                characterListVM.refreshCharacterList()
                if (characterListVM.allFilmsList.isEmpty()) {
                    characterListVM.fetchAllFilms()
                }
            }) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    when(characterListState.value.characterListFetchState) {
                        FetchState.REQUESTED -> {
                            items(10) {
                                CharacterShimmerView()
                            }
                        }
                        FetchState.SUCCESS -> {
                            if (characterListVM.charactersList.isNotEmpty()) {
                                items(characterListVM.charactersList) { character ->
                                    CharacterItem(
                                        name = character.name,
                                        birthday = character.birthYear,
                                        eyeColor = character.eyeColor
                                    ) {
                                        characterListVM.getFilmsForSelectedCharacter(character)
                                        navHostController.navigate(NavigationScreens.CharacterDetailScreen.route)
                                    }
                                }
                            } else {
                                item {
                                    Text(text = "No Characters here, Pull to refresh",
                                        color = Color.Red,
                                        modifier = Modifier.align(CenterHorizontally))
                                }
                            }
                        }
                        FetchState.FAILURE -> {
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

            Spacer(Modifier.weight(1f))
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextButton(
                        onClick = {
                            characterListVM.previousPageUrl?.let {
                                characterListVM.getCharactersList(
                                    it
                                )
                            }
                        },
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Yellow)
                        Text(text = "Prev", color = Color.Yellow, modifier = Modifier.padding(horizontal = 4.dp))
                    }
                    TextButton(
                        onClick = {
                            characterListVM.getCharactersList(characterListVM.nextPageUrl)
                        },
                    ) {
                        Text(text = "Next", color = Color.Yellow, modifier = Modifier.padding(horizontal = 4.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Back", tint = Color.Yellow)
                    }
                }
            }

            if (showFilterBottomSheet.value) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showFilterBottomSheet.value = false // Update showFilterBottomSheet variable name
                    },
                    sheetState = sheetState,
                    containerColor = Color.Black,
                    // contentColor is no longer needed as we'll set text color explicitly
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(color = Color.Black),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Text(
                            text = "Filter Character List By - ",
                            fontSize = 24.sp,
                            color = Yellow,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween // Since there are only 2 options
                        ) {
                            Text(
                                text = "Gender",
                                fontSize = 18.sp,
                                color = Yellow,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TextButton(
                                onClick = { characterListVM.filterCharacterListByMale() },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .background(color = Color.Transparent) // Optional styling
                            ) {
                                Text(
                                    text = "Male",
                                    fontSize = 16.sp,
                                    color = Yellow,
                                )
                            }
                            TextButton(
                                onClick = { characterListVM.filterCharacterListByFemale() },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .background(color = Color.Transparent) // Optional styling
                            ) {
                                Text(
                                    text = "Female",
                                    fontSize = 16.sp,
                                    color = Yellow,
                                )
                            }
                        }
                    }
                }

            }
            if (showSortBottomSheet.value) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showSortBottomSheet.value = false
                    },
                    sheetState = sheetState,
                    containerColor = Color.Black,
                    // contentColor is no longer needed as we'll set text color explicitly
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp) // Increased padding for better spacing
                            .background(color = Color.Black), // Ensure black background
                        horizontalAlignment = CenterHorizontally // Center content
                    ) {
                        Text(
                            text = "Sort Character List By - ",
                            fontSize = 24.sp,
                            color = Yellow, // Set text color to yellow
                            modifier = Modifier.padding(bottom = 16.dp) // Add spacing before options
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly // Evenly distribute options
                        ) {
                            TextButton(onClick = { characterListVM.sortCharactersListByName() }) {
                                Text(
                                    text = "Name",
                                    fontSize = 18.sp,
                                    color = Yellow,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            TextButton(onClick = { characterListVM.sortCharactersListByCreatedAt() }) {
                                Text(
                                    text = "Created At",
                                    fontSize = 18.sp,
                                    color = Yellow,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            TextButton(onClick = { characterListVM.sortCharactersListByUpdatedAt() }) {
                                Text(
                                    text = "Updated At",
                                    fontSize = 18.sp,
                                    color = Yellow,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}
