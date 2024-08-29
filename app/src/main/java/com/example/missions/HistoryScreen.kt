package com.example.missions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.missions.data.Mission
import com.example.missions.data.repository.MissionRepository
import com.example.missions.ui.theme.MissionsTheme

@Composable
fun HistoryScreen(
    repository: MissionRepository,
    //previousMissions: List<Mission>,
    //contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    var previousMissions: List<Mission> by remember { mutableStateOf(listOf()) }

    LaunchedEffect(scope) {
        previousMissions = repository.getCompletedMissions()
    }

    //val previousMissions = remember{ repository.getCompletedMissions() }
    if (previousMissions.size > 0) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(previousMissions) { mission ->
                MissionCard(
                    mission = mission,
                    date = if (mission.completed) {"Completed\n${convertToNormalDate(mission.dateCompleted)}"} else if (mission.failed) {"Failed\n${convertToNormalDate(mission.dateCompleted)}"} else {"Wtf: "},
                    previous = true,
                    modifier = Modifier
                )
            }
        }
    }
    else {
        NoHistoryScreen()
    }

}

@Composable
fun NoHistoryScreen(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            //.padding(dimensionResource(id = R.dimen.spacing_large))
    ) {
        Text(text = "No previous challenges...")

        Text(text = "Get a move on!")
    }

}

@Preview
@Composable
fun HistoryScreenPreview() {
    MissionsTheme(darkTheme = true) {
        //HistoryScreen(DataSource.missions, Modifier)
    }
}


