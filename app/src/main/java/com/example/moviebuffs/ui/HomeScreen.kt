package com.example.moviebuffs.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviebuffs.R
import com.example.moviebuffs.network.Movie
import com.example.moviebuffs.utils.MovieContentType

@Composable
fun HomeScreen(
    movieViewModel: MovieViewModel,
    retryAction: () -> Unit,
    contentType: MovieContentType,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val state by movieViewModel.uiState.collectAsState()

    when (movieViewModel.movieUiState) {
        is MovieUiState.Loading -> LoadingScreen(modifier)
        is MovieUiState.Success -> {
            when (contentType) {
                MovieContentType.lIST_ONLY -> MovieList(
                    movies = (movieViewModel.movieUiState as MovieUiState.Success).movies,
                    onMovieClick = {
                        movieViewModel.updateCurrentMovie(it)
                        movieViewModel.navigateToDetailPage()
                    },
                    contentPadding = contentPadding,
                    modifier = modifier
                )

                MovieContentType.lIST_AND_DETAIL -> MovieListDetailAdaptive(
                    movies = (movieViewModel.movieUiState as MovieUiState.Success).movies,
                    onMovieClick = {
                        movieViewModel.updateCurrentMovie(it)
                    },
                    selectedMovie = state.currentMovie,
                    contentPadding = contentPadding,
                    modifier = modifier
                )
            }
        }

        is MovieUiState.Error -> ErrorScreen(retryAction, modifier)
        else -> {}
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(movie: Movie, onItemClick: (Movie) -> Unit, modifier: Modifier = Modifier) {
    Card( modifier = modifier
        .padding(8.dp)
        .height(180.dp)
        .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            Log.d("MovieCard", "Clicked movie card: ${movie.title}")
            onItemClick(movie)
        })
    {
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(movie.poster)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(
                    R.string.movie_photo
                ),
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .padding(end = 4.dp)
                    .width(120.dp)
            )
            Column(modifier = modifier.padding(end = 2.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = modifier.height(4.dp))
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Spacer(modifier = modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_star_24),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = modifier.padding(end = 2.dp),
                        contentDescription = ""
                    )
                    Text(text = movie.reviewScore, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun MovieList(movies: List<Movie>, modifier: Modifier = Modifier, contentPadding: PaddingValues = PaddingValues(0.dp), onMovieClick: (Movie) -> Unit) {
    LazyColumn(modifier = modifier, contentPadding = contentPadding) {
        items(movies) { movie ->
            MovieCard(movie = movie, onItemClick = onMovieClick)
        }
    }
}

@Composable
fun MovieDetailScreen(
    selectedMovie: Movie,
    onBackPressed: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
){
    Log.d("MovieDetailScreen", "Recomposing for ${selectedMovie.title}")
    BackHandler {
        onBackPressed()
    }
    Column (modifier = modifier
        .fillMaxWidth()
        .padding(contentPadding)
        .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(selectedMovie.bigImage).crossfade(true).build(), error = painterResource(
            R.drawable.ic_broken_image), placeholder = painterResource(R.drawable.loading_img), contentDescription = "", modifier = modifier
            .fillMaxWidth()
            .height(350.dp), contentScale = ContentScale.Crop)
        Column (modifier = modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = selectedMovie.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(painter = painterResource(R.drawable.baseline_info_24), tint = MaterialTheme.colorScheme.secondary, contentDescription = "", modifier = Modifier.padding(end = 2.dp))
                Text(text = selectedMovie.contentRating, style = MaterialTheme.typography.titleMedium)
                Text(text = " | ")
                Text(text = selectedMovie.length, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(painter = painterResource(R.drawable.baseline_calendar_month_24), tint = MaterialTheme.colorScheme.secondary, contentDescription = "", modifier = Modifier.padding(end = 2.dp))
                Text(text = selectedMovie.releaseDate, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(painter = painterResource(R.drawable.baseline_star_24), tint = MaterialTheme.colorScheme.secondary, contentDescription = "", modifier = Modifier.padding(end = 2.dp))
                Text(text = selectedMovie.reviewScore, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = selectedMovie.description, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun MovieListDetailAdaptive(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    selectedMovie: Movie?,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(top = 16.dp)) {
        MovieList(
            movies = movies,
            onMovieClick = {
                Log.d("MovieListDetailAdaptive", "Clicked: ${it.title}")
                onMovieClick(it)
            },
            contentPadding = contentPadding,
            modifier = modifier.weight(2f)
        )
        if (selectedMovie != null) {
            Log.d("MovieListDetailAdaptive", "Recomposing")
            MovieDetailScreen(
                selectedMovie = selectedMovie,
                onBackPressed = {},
                contentPadding = contentPadding,
                modifier = modifier
                    .weight(3f)
                    .fillMaxHeight()
            )
        } else {
            Spacer(modifier = modifier.weight(3f))
        }
    }
}

@Preview
@Composable
fun MovieCardPreview() {
    MovieCard(
        movie = Movie(
            title = "Interstellar",
            poster = "",
            description = "From director Christopher Nolan (Inception) comes the story of ex-pilot Cooper (Matthew McConaughey), who must leave his family and Earth behind to lead an expedition beyond this galaxy to discover whether mankind has a future among the stars.",
            releaseDate = "2014-10-26",
            contentRating = "PG-13",
            reviewScore = "8.7",
            bigImage = "",
            length = "2h 49min"

        ),
        onItemClick = {}
    )
}

@Preview
@Composable
fun MovieListPreview() {
    MovieList(movies = listOf(Movie(title = "Interstellar", poster = "", description = "From director Christopher Nolan (Inception) comes the story of ex-pilot Cooper (Matthew McConaughey), who must leave his family and Earth behind to lead an expedition beyond this galaxy to discover whether mankind has a future among the stars.", releaseDate = "2014-10-26", contentRating = "PG-13", reviewScore = "8.7", bigImage = "", length = "2h 49min"), Movie(title = "Interstellar", poster = "", description = "From director Christopher Nolan (Inception) comes the story of ex-pilot Cooper (Matthew McConaughey), who must leave his family and Earth behind to lead an expedition beyond this galaxy to discover whether mankind has a future among the stars.", releaseDate = "2014-10-26", contentRating = "PG-13", reviewScore = "8.7", bigImage = "", length = "2h 49min"), Movie(title = "Interstellar", poster = "", description = "From director Christopher Nolan (Inception) comes the story of ex-pilot Cooper (Matthew McConaughey), who must leave his family and Earth behind to lead an expedition beyond this galaxy to discover whether mankind has a future among the stars.", releaseDate = "2014-10-26", contentRating = "PG-13", reviewScore = "8.7", bigImage = "", length = "2h 49min"), Movie(title = "Interstellar", poster = "", description = "From director Christopher Nolan (Inception) comes the story of ex-pilot Cooper (Matthew McConaughey), who must leave his family and Earth behind to lead an expedition beyond this galaxy to discover whether mankind has a future among the stars.", releaseDate = "2014-10-26", contentRating = "PG-13", reviewScore = "8.7", bigImage = "", length = "2h 49min"), Movie(title = "Interstellar", poster = "", description = "From director Christopher Nolan (Inception) comes the story of ex-pilot Cooper (Matthew McConaughey), who must leave his family and Earth behind to lead an expedition beyond this galaxy to discover whether mankind has a future among the stars.", releaseDate = "2014-10-26", contentRating = "PG-13", reviewScore = "8.7", bigImage = "", length = "2h 49min"), Movie(title = "Interstellar", poster = "", description = "From director Christopher Nolan (Inception) comes the story of ex-pilot Cooper (Matthew McConaughey), who must leave his family and Earth behind to lead an expedition beyond this galaxy to discover whether mankind has a future among the stars.", releaseDate = "2014-10-26", contentRating = "PG-13", reviewScore = "8.7", bigImage = "", length = "2h 49min")), onMovieClick = {}, modifier = Modifier)
}

@Preview
@Composable
fun MovieDetailPreview() {
    MovieDetailScreen(selectedMovie = Movie(title = "Interstellar", poster = "", description = "From director Christopher Nolan (Inception) comes the story of ex-pilot Cooper (Matthew McConaughey), who must leave his family and Earth behind to lead an expedition beyond this galaxy to discover whether mankind has a future among the stars.", releaseDate = "2014-10-26", contentRating = "PG-13", reviewScore = "8.7", bigImage = "", length = "2h 49min"), onBackPressed = { /*TODO*/ }, contentPadding = PaddingValues(0.dp), modifier = Modifier)
}