@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.speechify.composeuichallenge.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope> {
    error("LocalSharedTransitionScope not found")
}

val LocalAnimatedContentScope = compositionLocalOf<AnimatedContentScope> {
    error("LocalAnimatedContentScope not found")
}
