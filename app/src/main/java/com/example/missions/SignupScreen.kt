package com.example.missions

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.missions.data.User
import com.example.missions.network.MissionsApi
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var userExists by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var emailExists by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var confirmedPassword by remember { mutableStateOf("") }

    var signedUp by remember { mutableStateOf(false) }

    if (signedUp) {
        SignupSuccessScreen()
    }
    else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Sign up",
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    userExists = false
                },
                label = { Text("Username") },
                isError = userExists,
                supportingText = { if (userExists) Text("An account with this name already exists") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailExists = false
                                },
                label = { Text("Email address") },
                isError = emailExists,
                supportingText = { if (emailExists) Text("An account with this email already exists") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    /**/
                                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmedPassword,
                onValueChange = { confirmedPassword = it },
                label = { Text("Confirm password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = password != confirmedPassword,
                supportingText = { if (password != confirmedPassword) Text("Passwords do not match") }
                /* TODO: Make confirm password not show error instantly */
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (password == confirmedPassword && password != "" && username != "" && email != "") {
                        scope.launch {
                            try {
                                val response = MissionsApi.retrofitService.postUser(User(username, email, password))
                                Log.i("SIGNUP", "Response: $response")
                                if (response == "username exists") {
                                    userExists = true
                                    Log.e("SIGNUP", "Error: User already exists")
                                }
                                else if (response == "email exists") {
                                    emailExists = true
                                    Log.e("SIGNUP", "Error: Email already exists")
                                }
                                else {
                                    signedUp = true
                                    username = ""
                                    email = ""
                                    password = ""
                                    confirmedPassword = ""
                                }
                            }
                            catch (e: Exception) {
                                Log.e("SIGNUP", "Error: ${e.message}")
                            }
                        }
                    }
                }
            ) {
                Text("Sign up")
            }
        }
    }

}

@Composable
fun SignupSuccessScreen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Signup successful")
    }
}