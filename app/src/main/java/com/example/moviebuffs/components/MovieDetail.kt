package com.example.moviebuffs.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviebuffs.R
import com.example.moviebuffs.network.MoviePhoto


@Composable
fun MovieDetail(photo: MoviePhoto, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(photo.bigImageUrl)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_broken_image),
            placeholder = painterResource(id = R.drawable.loading_img),
            contentDescription = stringResource(id = R.string.movie_photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(350.dp)
        )
        Text(
            text = photo.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Row(
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "info",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Text(
                text = photo.contentRating,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
            Text(
                text = " | " + photo.length,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
        }
        Row(
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "release date",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Text(
                text = photo.releaseDate,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
        }
        Row(
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "star",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Text(
                text = photo.contentRating,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
        }
        Text(
            text = photo.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
