package com.example.bookreader.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.R
import com.example.bookreader.models.UserModel
import com.example.bookreader.navigation.ReaderScreens
import com.example.bookreader.screens.home.HomeViewModel
import com.example.bookreader.util.navigateToDestinationAndRemovePrevious

@Composable
fun DrawerMenuContent(
    user: UserModel,
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    ModalDrawerSheet {
        ProfileUi(user)
        Spacer(modifier = Modifier.height(10.dp))
        BioRow(user.bio)
        Divider(modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        DrawerItem(
            icon = Icons.Default.Settings,
            label = "Setting",
            onClick = {
                // TODO: navigate to About Settings
            }
        )
        DrawerItem(
            icon = Icons.Default.Info,
            label = "About",
            onClick = {
                // TODO: navigate to About Screen
            }
        )
        DrawerItem(
            icon = Icons.Default.ExitToApp,
            label = "Logout",
            onClick = {
                homeViewModel.logout(user)
                navController.navigateToDestinationAndRemovePrevious(
                    ReaderScreens.LoginScreen.name,
                    ReaderScreens.HomeScreen.name
                )
            }
        )
    }
}

@Composable
fun ProfileUi(
    user: UserModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(300.dp)
            .background(
                color = Color.White.copy(alpha = 0.5f)
            )
    ) {
        val imageResId = R.drawable.profile
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = imageResId.toString(),
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 6.dp
                )
                .fillMaxWidth()
                .requiredHeight(250.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    top = 20.dp,
                    bottom = 6.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Text(
                text = user.firstName + " " + user.lastName,
                style = TextStyle(
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
            )
            Text(
                text = user.email,
                modifier = Modifier.padding(top = 3.dp),
                style = TextStyle(
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Composable
fun BioRow(bio: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = bio ?: "",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 17.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
            Text(
                text = "Bio",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 13.sp
                ),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        modifier = Modifier
            .fillMaxWidth(),
        icon = {
            Icon(
                icon,
                icon.name,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        label = {
            Text(text = label, fontSize = 18.sp)
        },
        selected = false,
        onClick = {
            onClick.invoke()
        })
}
