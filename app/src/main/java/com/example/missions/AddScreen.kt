package com.example.missions

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.missions.data.Mission
import com.example.missions.data.MissionRepository
import com.example.missions.data.PostMission
import com.example.missions.network.MissionsApi
import com.example.missions.ui.theme.MissionsTheme
import kotlinx.coroutines.launch

@Composable
fun AddScreen(
    repository: MissionRepository,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    var text by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("") }
    var agree by remember {mutableStateOf(false)}
    var added by remember {mutableStateOf(false)}

    if (added) {
        MissionAddedScreen()
    }
    else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
            //.padding(contentPadding)
        ) {
            //Text(
                //text = "New challenge",
                //style = MaterialTheme.typography.headlineLarge
            //)

            //Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Enter your custom challenge",
                //style = MaterialTheme.typography.h4
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = text,
                onValueChange = {text = it},
                label = { Text("Enter challenge") }
            )
            Spacer(modifier = Modifier.height(64.dp))

            Text(text = "Select the difficulty level of this challenge")

            Spacer(modifier = Modifier.height(16.dp))

            DropDownMenu(
                items = listOf("Easy", "Medium", "Hard"),
                onValueChange = {difficulty = it},
                selectedOption = difficulty
            )

            Spacer(modifier = Modifier.height(64.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agree,
                    onCheckedChange = {agree = !agree}
                )
                Text(text = "Allow others to be given this challenge")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (text != "" && difficulty != "") {
                        scope.launch {
                            repository.insertMission(Mission(text = text, difficulty = difficulty))

                            if (agree) {
                                Log.i("POST_MISSION", "Agreed to post")
                                try {
                                    MissionsApi.retrofitService.postMission(PostMission(text = text, difficulty = difficulty))
                                    Log.i("POST_MISSION", "Success")
                                } catch (e: Exception) {
                                    Log.e("POST_MISSION", "Error: ${e.message}")
                                }
                            }
                            else {
                                Log.i("POST_MISSION", "Did not agree to post")
                            }
                        }
                        added = true
                    }
                    /*
                    if (agree) {
                                try {
                                    MissionsApi.retrofitService.postMission(Mission(text = text, difficulty = difficulty))

                                    Log.i("POST_MISSION", "Success")
                                } catch (e: Exception) {
                                    Log.e("POST_MISSION", "Error: ${e.message}")
                                }
                            }
                            else {
                                Log.i("POST_MISSION", "Not posted")
                            }
                    */
                },
            ) {
                Text("Add challenge")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    text = ""
                    difficulty = ""
                    agree = false
                },
            ) {
                Text("Clear")
            }

            /*

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Mission") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = difficulty,
                onValueChange = { difficulty = it },
                label = { Text("Difficulty") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    DataSource.addMission(Mission(text, difficulty))
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add")
            }
            */
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    items: List<String>,
    onValueChange: (String) -> Unit,
    selectedOption: String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded}
    ) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false}
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {Text(item)},
                    onClick = {
                        //selectedOption = item
                        expanded = false
                        onValueChange(item)
                    }
                )
            }
        }
    }
}

@Composable
fun MissionAddedScreen(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Challenge added!")
    }
}

@Composable
fun AddMissionForm() {

}

@Preview
@Composable
fun AddScreenPreview() {
    MissionsTheme(darkTheme = true) {
        //AddScreen(modifier = Modifier)
    }
}