package com.example.missions

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.missions.data.DataSource
import com.example.missions.data.repository.MissionRepository
import com.example.missions.data.datastore.UserPreferencesRepository
import com.example.missions.data.datastore.UserPreferencesRepositorySingleton
import com.example.missions.ui.theme.MissionsTheme
import com.example.missions.workers.scheduleMissionChange
import com.example.missions.workers.scheduleNotification
import kotlinx.coroutines.launch

const val USER_PREFERENCE_NAME = "user_preferences"
val Context.userPreferencesDataStore by preferencesDataStore(name = USER_PREFERENCE_NAME)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WorkManager.getInstance(this).cancelAllWork()
        //WorkManager.getInstance(this).pruneWork()
        val missionRepository = MissionRepository(this)

        lifecycleScope.launch {
            if (missionRepository.isEmpty()) {
                DataSource.addMissionsToDatabase(missionRepository)
                Log.i("DATABASE_INIT", "Database populated")
            }
            else {
                Log.i("DATABASE_INIT", "Database is not empty")
            }
        }

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
        scheduleMissionChange(this)
    }

    override fun onResume() {
        super.onResume()

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

