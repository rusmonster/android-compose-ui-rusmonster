@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.speechify.composeuichallenge.ui.view

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.speechify.composeuichallenge.data.Book
import com.speechify.composeuichallenge.ui.LocalAnimatedContentScope
import com.speechify.composeuichallenge.ui.LocalSharedTransitionScope

@Composable
fun BookImageView(book: Book, modifier: Modifier = Modifier) {
    val transitionScope = LocalSharedTransitionScope.current

    with(transitionScope) {
        AsyncImage(
            modifier = modifier
                .sharedElement(
                    transitionScope.rememberSharedContentState("image-${book.id}"),
                    animatedVisibilityScope = LocalAnimatedContentScope.current,
                ),
            model = book.imageUrl,
            contentDescription = book.name,
        )
    }
}
