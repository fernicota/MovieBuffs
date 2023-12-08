package com.example.moviebuffs.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebuffs.network.Movie
import com.example.moviebuffs.network.MovieApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface MovieUiState {
    data class Success(val movies: List<Movie>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}

data class UiState(
    val currentMovie: Movie?,
    val isShowingListPage: Boolean = true
)


class MovieViewModel : ViewModel() {
    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    private val _uiState = MutableStateFlow(UiState(currentMovie = null, isShowingListPage = true))

    val uiState: StateFlow<UiState> = _uiState

    init {
        getMoviesDetails()
    }

    fun updateCurrentMovie(selectedMovie: Movie) {
        _uiState.update {
            it.copy(currentMovie = selectedMovie)
        }
        Log.d("MovieViewModel", "Current Movie Updated: ${selectedMovie.title}")
    }

    fun navigateToListPage() {
        _uiState.update {
            it.copy(isShowingListPage = true)
        }
    }

    fun navigateToDetailPage() {
        _uiState.update {
            it.copy(isShowingListPage = false)
        }
    }


    fun getMoviesDetails() {
        viewModelScope.launch {
            movieUiState = try {
                val listResult = MovieApi.retrofitService.getMovies()
                MovieUiState.Success(listResult)

            } catch (e: Exception) {
                MovieUiState.Error
            }
        }
    }
}