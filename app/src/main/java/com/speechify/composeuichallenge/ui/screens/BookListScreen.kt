package com.speechify.composeuichallenge.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.speechify.composeuichallenge.R
import com.speechify.composeuichallenge.data.Book
import com.speechify.composeuichallenge.ui.view.BookImageView
import com.speechify.composeuichallenge.ui.view.LoadingView
import com.speechify.composeuichallenge.viewmodel.BookListActions
import com.speechify.composeuichallenge.viewmodel.BookListState
import com.speechify.composeuichallenge.viewmodel.BookListViewModel
import java.util.Locale

@Composable
fun BookListScreen(
    onBookClick: (String) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            Body(onBookClick)
        }
    }
}

@Composable
private fun Body(onBookClick: (String) -> Unit) {
    val viewModel = hiltViewModel<BookListViewModel>()
    val state by viewModel.state.collectAsState()

    when (val localState = state) {
        is BookListState.Loading -> LoadingView()

        is BookListState.Data -> BookList(
            dataState = localState,
            onFilterChanged = { filter ->
                viewModel.sendAction(BookListActions.OnSearchChanged(filter))
            },
            onBookClick = onBookClick,
        )
    }
}

@Composable
private fun BookList(
    dataState: BookListState.Data,
    onFilterChanged: (String) -> Unit,
    onBookClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = dataState.filter,
            singleLine = true,
            placeholder = { Text(stringResource(R.string.search_for_books)) },
            shape = ShapeDefaults.Medium,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = onFilterChanged
        )

        Spacer(Modifier.size(10.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(dataState.books, key = { it.id }) {
                BookListItem(it, onBookClick)
            }
        }
    }
}

@Composable
fun LazyItemScope.BookListItem(book: Book, onClick: (String) -> Unit) {
    ListItem(
        modifier = Modifier
            .height(120.dp)
            .animateItem(),
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BookImageView(
                    modifier = Modifier
                        .width(75.dp)
                        .height(100.dp),
                    book = book,
                )

                Spacer(Modifier.size(10.dp))

                Description(book)

                Spacer(Modifier.size(10.dp))

                Button(
                    onClick = {
                        onClick(book.id)
                    }
                ) {
                    Text(stringResource(R.string.details))
                }
            }
        }
    )
}

@Composable
private fun RowScope.Description(book: Book) {
    Column(
        modifier = Modifier.weight(1f)
    ) {
        Text(
            text = book.name,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.size(10.dp))

        Text(
            text = book.author,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.size(10.dp))

        Text(
            text = String.format(Locale.getDefault(), "%.2f (%d)", book.rating, book.reviewCount),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
