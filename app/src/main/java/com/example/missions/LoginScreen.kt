package com.example.missions

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.missions.data.User
import com.example.missions.network.MissionsApi
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier
) {
    val scope = rememberCoroutineScope()

    var infoText by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loggedIn by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Log in")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = false,
            supportingText = { Text("") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            isError = false,
            supportingText = { Text("") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email != "" && password != "") {
                    scope.launch {
                        try {
                            val response = MissionsApi.retrofitService.postLogin(User(username = "", email = email, password = password))

                            if (response == "login success") {
                                loggedIn = true
                                Log.i("LOGIN", "Logged in")
                                //TODO: Navigate to profile screen
                            }
                            else if (response == "incorrect password") {
                                Log.i("LOGIN", "Incorrect password")
                            }
                            else if (response == "email not found") {
                                Log.i("LOGIN", "Email not found")
                            }
                            else {
                                Log.i("LOGIN", "wtf")
                            }
                        } catch (e: Exception) {
                            Log.e("LOGIN", "Error: ${e.message}")
                        }

                    }
                }
            }
        ) {
            Text("Log in")
        }

        Text(
            text = infoText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}