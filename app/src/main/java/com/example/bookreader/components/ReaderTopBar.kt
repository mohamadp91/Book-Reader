package com.example.bookreader.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookreader.R
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.widgets.CustomIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(
    title: String = "Book Reader",
    navController: NavController,
    screen: ReaderScreens = ReaderScreens.HomeScreen,
    onNavigateButtonClicked: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (screen == ReaderScreens.HomeScreen)
                    Icon(
                        painter = painterResource(id = R.drawable.open_book),
                        contentDescription = "Open Book",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(horizontal = 3.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
            }
        },
        navigationIcon = {
            when (screen) {
                ReaderScreens.HomeScreen -> {
                    CustomIconButton(icon = Icons.Default.Menu,
                        onClick = {
                            onNavigateButtonClicked.invoke()
                        })
                }
                ReaderScreens.UserScreen -> {}
                else -> {
                    CustomIconButton(icon = Icons.Default.ArrowBack,
                        onClick = {
                            navController.popBackStack()
                        })
                }
            }
        }
    )
}