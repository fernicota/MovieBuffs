package com.example.moviebuffs.utils_view_state

import com.example.moviebuffs.network.MoviePhoto

data class MoviesUiState(
    val moviesList: List<MoviePhoto> = emptyList(),
    val currentMovie: MoviePhoto? = null,
    val isShowingListPage: Boolean = true
)