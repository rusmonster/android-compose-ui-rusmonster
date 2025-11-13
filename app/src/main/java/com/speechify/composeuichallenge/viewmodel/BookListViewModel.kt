package com.speechify.composeuichallenge.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speechify.composeuichallenge.data.Book
import com.speechify.composeuichallenge.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
sealed interface BookListState {
    @Immutable
    object Loading : BookListState

    @Immutable
    data class Data(
        val books: List<Book> = emptyList()
    ) : BookListState
}

sealed interface BookListActions {
    data class OnSearchChanged(val filter: String) : BookListActions
}

@HiltViewModel
class BookListViewModel @Inject constructor(
    val booksRepository: BooksRepository
): ViewModel() {
    private val _state = MutableStateFlow<BookListState>(BookListState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            onInit()
        }
    }

    fun sendAction(action: BookListActions) {
        viewModelScope.launch {
            executeAction(action)
        }
    }

    private suspend fun executeAction(action: BookListActions) {
        when (action) {
            is BookListActions.OnSearchChanged -> onSearchChanged(action)
            // Add new actions here
        }
    }

    private suspend fun onInit() {
        val books = booksRepository.getBooks()

        _state.value = BookListState.Data(
            books = books
        )
    }

    private var searchJob: Job? = null

    private fun onSearchChanged(action: BookListActions.OnSearchChanged) {
        val dataState = _state.value as? BookListState.Data ?: return
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val filteredBooks = booksRepository.searchBook(action.filter)
            _state.value = dataState.copy(
                books = filteredBooks
            )
        }
    }
}