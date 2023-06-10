package com.example.bookreader.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookreader.navigation.ReaderScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(
    title: String,
    onButtonClicked: () -> Unit,
    navController: NavController,
    screen: ReaderScreens = ReaderScreens.HomeScreen,
) {
    val shouldOpenMenu = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    TopAppBar(title = {
        Text(
            text = title,
            color = Color.DarkGray,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        )
    },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .shadow(6.dp),
        actions = {
            if (screen == ReaderScreens.HomeScreen) {
                IconButton(onClick = {
                    shouldOpenMenu.value = true
                }) {
                    Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "")
                }
//                MoreDropDownMenu(shouldMenuOpen = shouldOpenMenu, navController = navController)
            } else {
                Box {}
            }

        }, navigationIcon = {

            when (screen) {
                ReaderScreens.UserScreen -> {}
                ReaderScreens.HomeScreen -> {}
                else -> {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier.clickable { onButtonClicked.invoke() },
                        tint = Color.Gray
                    )
                }
            }
        }
    )
}
