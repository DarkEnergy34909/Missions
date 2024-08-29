package com.example.missions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var loggedIn by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (loggedIn) {
            // TODO: Profile screen
            Text("TODO: Profile screen")
        }
        else {
            Text(
                text = "You are currently not logged in.",
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Log in or sign up to access your profile and view friends' profiles.",
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(96.dp))

            Button(onClick = {/*navController.navigate("MissionScreens.SignUp.name")*/}) {
                Text("Sign up")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {/*navController.navigate("MissionScreens.LogIn.name")*/}) {
                Text("Log in")
            }


        }
    }

}