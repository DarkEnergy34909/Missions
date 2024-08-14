package com.example.missions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.missions.data.DataSource
import com.example.missions.data.Mission
import com.example.missions.ui.theme.MissionsTheme

@Composable
fun HistoryScreen(
    previousMissions: List<Mission>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(contentPadding = contentPadding) {
        items(previousMissions) { mission ->
            MissionCard(
                mission = mission,
                date = if (mission.completed == false) {"Failed: "} else {"Completed: "}, // TODO: Implement date
                previous = true,
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    MissionsTheme(darkTheme = true) {
        HistoryScreen(DataSource.missions, PaddingValues(0.dp))
    }
}
