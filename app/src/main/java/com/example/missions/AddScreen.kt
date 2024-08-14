package com.example.missions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.missions.data.Mission
import com.example.missions.ui.theme.MissionsTheme

@Composable
fun AddScreen(
    contentPadding: PaddingValues
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Text(
            text = "Add a new challenge:",
            //style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(16.dp))
        var text by remember { mutableStateOf("") }
        var difficulty by remember { mutableStateOf("") }

        OutlinedTextField(
            value = text,
            onValueChange = {text = it},
            label = { Text("Enter your challenge") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "What is the difficulty level of this challenge?")

        Spacer(modifier = Modifier.height(16.dp))

        DropDownMenu(items = listOf("Easy", "Medium", "Hard"))


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(items: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("")}

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
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false}
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {Text(item)},
                    onClick = {
                        selectedOption = item
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AddScreenPreview() {
    MissionsTheme(darkTheme = true) {
        AddScreen(contentPadding = PaddingValues(0.dp))
    }
}