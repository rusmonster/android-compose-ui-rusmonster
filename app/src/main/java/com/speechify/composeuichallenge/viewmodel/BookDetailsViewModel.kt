package com.speechify.composeuichallenge.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speechify.composeuichallenge.data.Book
import com.speechify.composeuichallenge.repository.BooksRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Immutable
sealed interface BookDetailsState {
    @Immutable
    object Loading : BookDetailsState

    @Immutable
    data class Data(
        val book: Book,
    ) : BookDetailsState
}

sealed interface BookDetailsEvent {
    object BookNotFound : BookDetailsEvent
}

@HiltViewModel(assistedFactory = BookDetailsViewModel.Factory::class)
class BookDetailsViewModel @AssistedInject constructor(
    val booksRepository: BooksRepository,
    @Assisted
    val bookId: String,
) : ViewModel() {
    private val _state = MutableStateFlow<BookDetailsState>(BookDetailsState.Loading)
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<BookDetailsEvent>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch { onInit() }
    }

    private suspend fun onInit() {
        val book = booksRepository.getBook(bookId)

        if (book == null) {
            _events.emit(BookDetailsEvent.BookNotFound)
            return
        }

        _state.value = BookDetailsState.Data(book)
    }

    @AssistedFactory
    interface Factory {
        fun create(bookId: String): BookDetailsViewModel
    }
}