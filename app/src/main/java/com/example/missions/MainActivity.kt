package com.example.missions

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import com.example.missions.data.MissionRepository
import com.example.missions.data.UserPreferencesRepository
import com.example.missions.data.UserPreferencesRepositorySingleton
import com.example.missions.ui.theme.MissionsTheme
import com.example.missions.workers.scheduleNotification

const val USER_PREFERENCE_NAME = "user_preferences"
val Context.userPreferencesDataStore by preferencesDataStore(name = USER_PREFERENCE_NAME)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val missionRepository = MissionRepository(this)
        val userPreferencesRepository = UserPreferencesRepositorySingleton.getInstance(this)

        enableEdgeToEdge()
        setContent {
            MissionsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MissionScreen(
                        missionRepository = missionRepository,
                        userPreferencesRepository = userPreferencesRepository
                    )
                }

            }
        }
        scheduleNotification(this)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MissionsTheme(darkTheme = true) {
        MissionScreen(
            missionRepository = MissionRepository(LocalContext.current),
            userPreferencesRepository = UserPreferencesRepository(LocalContext.current.userPreferencesDataStore)
        )
    }
}

