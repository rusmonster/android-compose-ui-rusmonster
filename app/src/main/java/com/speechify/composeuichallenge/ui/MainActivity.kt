package com.speechify.composeuichallenge.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.speechify.composeuichallenge.ui.screens.BookListScreen
import com.speechify.composeuichallenge.ui.theme.ComposeUIChallengeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeUIChallengeTheme {
                App()
            }
        }
    }
}

sealed interface Screens {
    @Serializable
    object BookList : Screens

    @Serializable
    data class BookDetails(val bookId: String)
}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.BookList) {
        composable<Screens.BookList> {
            BookListScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screens.BookDetails(bookId))
                }
            )
        }

        composable<Screens.BookDetails> {
            // TODO
        }
    }
}

@Composable
private fun Greeting(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Replace me with actual UI",
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    ComposeUIChallengeTheme {
        Greeting()
    }
}
