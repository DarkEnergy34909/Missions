package com.example.missions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.missions.data.User
import com.example.missions.network.MissionsApi
import kotlinx.coroutines.launch

@Composable
fun MoreScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    var displayedText by remember { mutableStateOf("Loading...") }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(scope) {
        try {
            val result = MissionsApi.retrofitService.getUserData()
            displayedText = result
        } catch (e: Exception) {
            displayedText = "Error: ${e.message}"
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                /*scope.launch{
                    try {
                        MissionsApi.retrofitService.postUser(User(username, password))
                        displayedText = "User created"
                    } catch (e: Exception) {
                        displayedText = "Error: ${e.message}"
                    }
                }*/
            }
        ) {
            Text("Sign up")
        }

        Text(
            text = displayedText,
            textAlign = TextAlign.Center
        )

    }
}