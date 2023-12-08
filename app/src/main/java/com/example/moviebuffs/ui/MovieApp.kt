package com.example.moviebuffs.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviebuffs.utils.MovieContentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieApp(
    windowSize: WindowWidthSizeClass
) {
    val viewModel: MovieViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val contentType: MovieContentType
    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            contentType = MovieContentType.lIST_ONLY
        }

        WindowWidthSizeClass.Medium -> {
            contentType = MovieContentType.lIST_ONLY
        }

        WindowWidthSizeClass.Expanded -> {
            contentType = MovieContentType.lIST_AND_DETAIL
        } else -> {
            contentType = MovieContentType.lIST_ONLY
        }
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            MovieTopAppBar(
                onBackPressed = { viewModel.navigateToListPage() },
                isShowingListPage = uiState.isShowingListPage
            )
        }
    ) {innerPadding ->
        if (uiState.isShowingListPage) {
            HomeScreen(
                movieViewModel = viewModel,
                retryAction = viewModel::getMoviesDetails,
                contentType = contentType,
                contentPadding = innerPadding
            )
        } else {
            MovieDetailScreen(
                selectedMovie = uiState.currentMovie!!,
                onBackPressed = { viewModel.navigateToListPage() },
                contentPadding = innerPadding
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieTopAppBar(
    onBackPressed: () -> Unit,
    isShowingListPage: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "Movie Buffs"
            )
        },
        navigationIcon = if (!isShowingListPage) {
            {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        } else {
            { Box {} }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
    )
}