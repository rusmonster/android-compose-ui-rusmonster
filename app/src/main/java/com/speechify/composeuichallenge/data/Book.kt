package com.speechify.composeuichallenge.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Book(
    val id: String,
    val name: String,
    val author: String,
    val imageUrl: String,
    val description: String,
    val rating: Float,
    val reviewCount: Int
)
