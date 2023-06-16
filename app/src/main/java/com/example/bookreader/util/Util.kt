package com.example.bookreader.util

import androidx.navigation.NavController

fun NavController.navigateToDestinationAndRemovePrevious(
    destination: String,
    previousDestination: String
) {
    this.navigate(destination) {
        popUpTo(previousDestination) { inclusive = true }
    }
}

fun Collection<String?>?.joinToStringNullable(separator: String = ","): String =
    if (!this.isNullOrEmpty()) this.joinToString(separator) else ""