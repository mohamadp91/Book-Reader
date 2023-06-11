package com.example.bookreader.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.bookreader.widgets.CustomIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(
    title: String = "Book Reader",
    navController: NavController,
    isMainScreen: Boolean = false,
    onMenuClicked: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        },
        navigationIcon = {
            if (isMainScreen)
                CustomIconButton(icon = Icons.Default.Menu,
                    onClick = {
                        onMenuClicked.invoke()
                    })
            else
                CustomIconButton(icon = Icons.Default.ArrowBack,
                    onClick = {
                        navController.popBackStack()
                    })
        }
    )
}