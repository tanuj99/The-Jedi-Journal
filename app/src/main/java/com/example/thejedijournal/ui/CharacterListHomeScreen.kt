package com.example.thejedijournal.ui

import android.os.*
import android.util.*
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
import androidx.compose.ui.text.style.*
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
                NavHost(
                    navController = navHostController,
                    startDestination = NavigationScreens.CharacterListScreen.route
                ) {
                    composable(NavigationScreens.CharacterListScreen.route) {
                        HomeScreenBase(navHostController)
                    }
                    composable(NavigationScreens.CharacterDetailScreen.route) {
                        CharacterDetailScreen(navHostController, characterListVM = characterListVM)
                    }
                }
            }
        }
        if (coreServices.isDeviceOnline) {
            Log.d("HomeScreen", "onCreate: Fetching from Network")
            characterListVM.getInitialCharactersList()
        } else {
            Log.d("HomeScreen", "onCreate: Fetching from DB")
            characterListVM.getCharactersListFromDB()
            characterListVM.getFilmsListFromDB()
        }
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
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
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
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Settings",
                            tint = Color.Yellow
                        )
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
                                Icon(
                                    imageVector = Icons.Filled.List,
                                    contentDescription = "Filter",
                                    tint = Color.LightGray
                                )
                                Text(
                                    text = "Filter",
                                    color = Color.LightGray,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }

                            TextButton(
                                onClick = {
                                    showSortBottomSheet.value = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Settings,
                                    contentDescription = "Sort",
                                    tint = Color.LightGray
                                )
                                Text(
                                    text = "Sort",
                                    color = Color.LightGray,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .height(24.dp)
            )

            CharacterListGridView(characterListState, navHostController) {
                if (characterListState.value.loadMoreCharacterListFetchState != FetchState.REQUESTED)
                    characterListVM.loadMoreCharacters()
            }
            Spacer(
                modifier = Modifier
                    .height(24.dp)
            )

            if (characterListState.value.loadMoreCharacterListFetchState == FetchState.REQUESTED) {
                CircularProgressIndicator(modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(8.dp))
            }

            if (showFilterBottomSheet.value) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showFilterBottomSheet.value =
                            false
                    },
                    sheetState = sheetState,
                    containerColor = Color.Black,
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
                                onClick = {
                                    characterListVM.filterCharacterListByMale()
                                    showFilterBottomSheet.value = false
                                },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .background(color = Color.Transparent)
                            ) {
                                Text(
                                    text = "Male",
                                    fontSize = 16.sp,
                                    color = Yellow,
                                )
                            }
                            TextButton(
                                onClick = {
                                    characterListVM.filterCharacterListByFemale()
                                    showFilterBottomSheet.value = false
                                },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .background(color = Color.Transparent)
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
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(color = Color.Black),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Text(
                            text = "Sort Character List By - ",
                            fontSize = 24.sp,
                            color = Yellow, // Set text color to yellow
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TextButton(onClick = {
                                characterListVM.sortCharactersListByName()
                                showSortBottomSheet.value = false
                            }) {
                                Text(
                                    text = "Name",
                                    fontSize = 18.sp,
                                    color = Yellow,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            TextButton(onClick = {
                                characterListVM.sortCharactersListByCreatedAt()
                                showSortBottomSheet.value = false
                            }) {
                                Text(
                                    text = "Created At",
                                    fontSize = 18.sp,
                                    color = Yellow,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            TextButton(onClick = {
                                characterListVM.sortCharactersListByUpdatedAt()
                                showSortBottomSheet.value = false
                            }) {
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

    @Composable
    private fun CharacterListGridView(
        characterListState: State<CharacterListState>,
        navHostController: NavHostController,
        onEndReached: () -> Unit, // Fetch next page of character when we reach end of current list
    ) {

        val swipeRefreshState = rememberSwipeRefreshState(
            isRefreshing = characterListState.value.refreshCharacterList && coreServices.isDeviceOnline
        )

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
                when (characterListState.value.characterListFetchState) {
                    FetchState.REQUESTED -> {
                        items(15) {
                            CharacterShimmerView()
                        }
                    }

                    FetchState.SUCCESS -> {
                        if (characterListVM.charactersList.isNotEmpty()) {
                            items(characterListVM.charactersList.size) { i ->
                                val character = characterListVM.charactersList[i]

                                if (i >= characterListVM.charactersList.size - 1) {
                                    onEndReached()
                                }
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
                                Text(
                                    text = "No Characters here, Pull to refresh",
                                    color = Color.Red,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    FetchState.FAILURE -> {
                        item {
                            Text(
                                text = "Something went wrong, Please try again",
                                color = Color.Red,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}
