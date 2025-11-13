package com.speechify.composeuichallenge.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.speechify.composeuichallenge.R
import com.speechify.composeuichallenge.ui.LocalNavController
import com.speechify.composeuichallenge.ui.view.BookImageView
import com.speechify.composeuichallenge.ui.view.LoadingView
import com.speechify.composeuichallenge.viewmodel.BookDetailsEvent
import com.speechify.composeuichallenge.viewmodel.BookDetailsState
import com.speechify.composeuichallenge.viewmodel.BookDetailsViewModel

@Composable
fun BookDetailsScreen(bookId: String) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            Body(bookId)
        }
    }
}

@Composable
private fun Body(bookId: String) {
    val viewModel: BookDetailsViewModel = hiltViewModel { factory: BookDetailsViewModel.Factory ->
        factory.create(bookId)
    }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { it.handleEvent(context, navController) }
    }

    when (val localState = state) {
        is BookDetailsState.Loading -> LoadingView()

        is BookDetailsState.Data -> DataView(localState)
    }
}

@Composable
private fun DataView(dataState: BookDetailsState.Data) {
    val book = dataState.book

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BookImageView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            book = book,
        )

        Spacer(Modifier.size(16.dp))

        Text(
            text = book.name,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.size(16.dp))

        Text(
            text = book.author,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.size(16.dp))

        Text(
            text = book.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

private fun BookDetailsEvent.handleEvent(context: Context, navController: NavHostController) {
    when (this) {
        BookDetailsEvent.BookNotFound -> {
            Toast.makeText(context, R.string.book_not_found, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }
}
