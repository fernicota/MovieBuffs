package com.example.moviebuffs.utils_view_state

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebuffs.network.MovieApi
import com.example.moviebuffs.network.MoviePhoto
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MovieUiState {
    data class Success(val photos: List<MoviePhoto>) : MovieUiState
    data object Error : MovieUiState
    data object Loading : MovieUiState
}

class MoviesViewModel : ViewModel() {
    private var _movieUiState by mutableStateOf<MovieUiState>(MovieUiState.Loading)
    val movieUiState: MovieUiState get() = _movieUiState

    private val _currentMovie = mutableStateOf<MoviePhoto?>(null)
    val currentMovie: State<MoviePhoto?> = _currentMovie


    private val _isDetailScreenShowing = mutableStateOf(false)
    val isDetailScreenShowing: State<Boolean> = _isDetailScreenShowing

    fun showDetailScreen(show: Boolean) {
        _isDetailScreenShowing.value = show
    }

    init {
        getMoviePhotos()
    }

    fun getMoviePhotos() {
        viewModelScope.launch {
            _movieUiState = try {
                MovieUiState.Success(MovieApi.retrofitService.getPhotos())
            } catch (e: IOException) {
                MovieUiState.Error
            }
        }
    }

    fun setCurrentMovie(movie: MoviePhoto?) {
        _currentMovie.value = movie
    }
}