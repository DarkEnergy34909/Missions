package com.example.missions

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.CheckBox
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.missions.data.DataSource
import com.example.missions.data.Mission
import com.example.missions.data.MissionRepository
import com.example.missions.data.UserPreferencesRepository
import com.example.missions.data.getNewMission
import com.example.missions.ui.theme.MissionsTheme
import com.example.missions.workers.getDelay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

enum class MissionStates() {
    MISSION_UNDEFINED,
    MISSION_COMPLETED,
    MISSION_FAILED
}

enum class MissionScreens() {
    Home(),
    Add(),
    History(),
    More()
}

//const val USER_PREFERENCE_NAME = "user_preferences"
//val Context.userPreferencesDataStore by preferencesDataStore(name = USER_PREFERENCE_NAME)

//@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(InternalCoroutinesApi::class)
@Composable
fun MissionScreen(
    missionRepository: MissionRepository,
    userPreferencesRepository: UserPreferencesRepository,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //val userPreferencesRepository = remember { UserPreferencesRepository(context.userPreferencesDataStore) }
    val userStreak by userPreferencesRepository.streakFlow.collectAsStateWithLifecycle(initialValue = 0)

    //val missionRepository = remember{ MissionRepository(context) }

    val missionState by userPreferencesRepository.missionStateFlow.collectAsStateWithLifecycle(initialValue = MissionStates.MISSION_UNDEFINED.ordinal)

    //var currentMission: Mission by remember {mutableStateOf(DataSource.missions[Random.nextInt(0, DataSource.missions.size)])}

    var currentMission: Mission by remember {mutableStateOf(Mission(text = "Loading...", difficulty = "Easy", completed = false, dateCompleted = ""))}

    val currentMissionId by userPreferencesRepository.missionIdFlow.collectAsStateWithLifecycle(initialValue = 0)

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()



    LaunchedEffect(lifecycleState) {
        delay(200)
        Log.e("MISSION_ID_FUN", currentMissionId.toString())
        if (currentMissionId == 0) {
            Log.e("MISSION_ID_FUN", "IF")
            currentMission = getNewMission(missionRepository)
            //currentMission.dateCompleted = getDate()
            userPreferencesRepository.saveMissionId(missionId = currentMission.id)

            if (currentMission.dateCompleted != getDate()) {
                currentMission.dateCompleted = getDate()
                missionRepository.updateMission(currentMission)
            }
        }
        else {
            Log.e("MISSION_ID_FUN", "ELSE")
            currentMission = missionRepository.getMission(currentMissionId)
            if (currentMission.dateCompleted != getDate()) {
                currentMission.dateCompleted = getDate()
                missionRepository.updateMission(currentMission)
            }
        }

        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}

            Lifecycle.State.RESUMED -> {
                delay(200) // TODO: Find a better fix than this
                Log.e("MISSION_ID_FUN", currentMissionId.toString())
                if (currentMissionId == 0) {
                    Log.e("MISSION_ID_FUN", "IF")
                    currentMission = getNewMission(missionRepository)
                    userPreferencesRepository.saveMissionId(missionId = currentMission.id)
                } else {
                    Log.e("MISSION_ID_FUN", "ELSE")
                    currentMission = missionRepository.getMission(currentMissionId)
                }
            }
        }
    }



    /*LaunchedEffect(scope) {
        //userPreferencesRepository.saveMissionState(missionState = MissionStates.MISSION_UNDEFINED.ordinal)
        //DataSource.addMissionsToDatabase(repository)
        //Log.e("MISSION_ID_FUN", currentMissionId.toString())
        currentMission = getNewMission(missionRepository)
    }*/


    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MissionScreens.valueOf(
        backStackEntry?.destination?.route ?: MissionScreens.Home.name
    )

    Scaffold(
        topBar = {
            MissionAppBar(
                canNavigateBack = false,
                streak = userStreak
            )
        },
        bottomBar = {
            NavigationBar(
                navController = navController,
                currentScreen = currentScreen
            )
        },
        modifier = modifier
            .statusBarsPadding()
            .padding(dimensionResource(R.dimen.padding_small))
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MissionScreens.Home.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            composable(route = MissionScreens.Home.name) {
                HomeScreen(
                    repository = missionRepository,
                    streak = userStreak,
                    currentMission = currentMission,
                    missionState = missionState,
                    completeMission = {
                        //missionState = MissionStates.MISSION_COMPLETED.ordinal
                        currentMission.dateCompleted = getDate()
                        currentMission.completed = true
                        scope.launch{
                            missionRepository.updateMission(currentMission)

                            userPreferencesRepository.saveMissionState(missionState = MissionStates.MISSION_COMPLETED.ordinal)

                            userPreferencesRepository.saveStreak(streak = userStreak + 1)
                        }
                                      },

                    failMission = {
                        //missionState = MissionStates.MISSION_FAILED.ordinal
                        currentMission.failed = true
                        currentMission.dateCompleted = getDate()
                        scope.launch{
                            missionRepository.updateMission(currentMission)

                            userPreferencesRepository.saveMissionState(missionState = MissionStates.MISSION_FAILED.ordinal)

                            userPreferencesRepository.saveStreak(streak = 0)
                        }
                                  },

                    modifier = modifier
                )
            }
            composable(route = MissionScreens.Add.name) {
                AddScreen(
                    repository = missionRepository,
                    modifier = modifier
                )
            }
            composable(route = MissionScreens.History.name) {
                HistoryScreen(
                    repository = missionRepository,
                    //previousMissions = DataSource.missions,
                    modifier = modifier
                )
            }
            /*composable(route = MissionScreens.More.name) {
                MoreScreen()
            }*/
        }
    }

}

@Composable
fun HomeScreen(
    repository: MissionRepository,
    streak: Int,
    missionState: Int,
    completeMission: () -> Unit,
    failMission: () -> Unit,
    currentMission: Mission,
    //innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    //val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    //val date = Calendar.getInstance().time
    //val dateInString = dateFormatter.format(date)
    val dateInString = convertToNormalDate(getDate())
    var timeLeft by remember {mutableStateOf("")}
    //timeLeft.value = ""

    LaunchedEffect(scope) {
        while (true) {
            timeLeft = getTimeLeft()
            delay(1000)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            //.padding(innerPadding)
    ) {
        /*Row() {
            Spacer(modifier = Modifier.weight(2f))

            Streak(
                days = 1,
                modifier = Modifier.weight(1f)
            )
        }*/

        //Spacer(modifier = Modifier.size(256.dp))
        if (missionState == MissionStates.MISSION_UNDEFINED.ordinal) {

            Text(
                text = "Today's challenge:",
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(96.dp))

            MissionCard(
                mission = currentMission  /*DataSource.missions[Random.nextInt(0, DataSource.missions.size)]*/,
                date = dateInString,
                previous = false
            )

            SuccessFailureButtons(
                onFailButtonPressed = failMission     /*{missionState = MissionStates.MISSION_FAILED.ordinal}*/,
                onSuccessButtonPressed = completeMission  /*{missionState = MissionStates.MISSION_COMPLETED.ordinal}*/
            )

            Spacer(modifier = Modifier.height(96.dp))

            TimeLeft(
                timeLeft = timeLeft,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
        else if (missionState == MissionStates.MISSION_COMPLETED.ordinal) {
            MissionCompleteScreen(streak = streak)
        }
        else {
            MissionFailedScreen()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionAppBar(
    streak: Int,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Challenges")
        },
        //colors = ,
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
            else {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Menu"
                    )
                }
            }
        },
        actions = {
            //IconButton(onClick = {}) {
                //Icon(
                    //imageVector = Icons.Filled.Settings,
                    //contentDescription = "Settings"
                //)
            //}
            Streak(days = streak)
        }
    );
}

@Composable
fun NavigationBar(
    navController: NavHostController,
    currentScreen: MissionScreens,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        actions = {

            // Home button
            IconButton(
                onClick = {if (currentScreen != MissionScreens.Home) {navController.navigate(MissionScreens.Home.name)}},
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Home",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                }


            }

            // Add button
            IconButton(
                onClick = {if (currentScreen != MissionScreens.Add) {navController.navigate(MissionScreens.Add.name)}},
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Add",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // History button
            IconButton(
                onClick = {if (currentScreen != MissionScreens.History) {navController.navigate(MissionScreens.History.name)}},
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Checklist,
                        contentDescription = "History",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "History",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            /*
            // More button
            IconButton(
                onClick = {if (currentScreen != MissionScreens.More) {navController.navigate(MissionScreens.More.name)}},
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreHoriz,
                        contentDescription = "More",
                        modifier = Modifier.weight(1f)

                    )
                    Text(
                        text = "More",
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }*/
        },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .height(80.dp)
    )
}

@Composable
fun ActionButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    color: Color = Color.Red,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        content = {
            //Text(text = text);
            Icon(
                imageVector = imageVector,
                contentDescription = "I failed"
            )
        },
        colors = ButtonDefaults.buttonColors(color)
    )
}

@Composable
fun SuccessFailureButtons(
    onFailButtonPressed: () -> Unit,
    onSuccessButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Spacer(modifier = Modifier.weight(0.5f))

        ActionButton(
            imageVector = Icons.Filled.Close,
            onClick = onFailButtonPressed,
            color = Color.Red,
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
        )

        Spacer(modifier = Modifier.weight(2f))

        ActionButton(
            imageVector = Icons.Filled.Check,
            onClick = onSuccessButtonPressed,
            color = Color.Green,
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
        )

        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun MenuBar(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(dimensionResource(R.dimen.padding_small))
            //.background(MaterialTheme.colorScheme.primary)
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.weight(0.2f)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {},
            modifier = Modifier
                .weight(0.2f)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

}

@Composable
fun MissionCard(
    //missionText: String,
    //missionDifficulty: Int,
    mission: Mission,
    date: String,
    previous: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
                containerColor =  if (!previous) {MaterialTheme.colorScheme.surfaceVariant} else {if(mission.completed) {Color(0xFF2f633d)} else {MaterialTheme.colorScheme.errorContainer}},
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
            .animateContentSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small)),
                horizontalArrangement = Arrangement.Center,

            ) {
                Text(
                    text = date,
                    /* TODO style = ... */
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_small)
                        )
                )


                Text(
                    text = "Difficulty:",
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_small)
                        )
                )
                Text(
                    text = mission.difficulty,
                    textAlign = TextAlign.End,
                    color = when (mission.difficulty) {
                        "Easy" -> Color.Green
                        "Medium" -> Color.Yellow
                        "Hard" -> Color.Red
                        else -> Color.Unspecified
                    },
                    modifier = Modifier
                        //.weight(1f)
                        .padding(/*start = dimensionResource(R.dimen.padding_small), */end = dimensionResource(R.dimen.padding_small))
                )

            }
            Text(
                text = mission.text,
                /* TODO style = ... */
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_large))
            )
        }
    }
}

@Composable
fun TimeLeft(
    timeLeft: String,
    modifier: Modifier = Modifier
) {
    Text(text = timeLeft)
}

@Composable
fun Streak(
    days: Int = 0,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = "Streak",
            tint = if (days == 0) {MaterialTheme.colorScheme.onPrimaryContainer} else {Color(0xFFFFA500)}
        )
        
        Text(
            text = days.toString(),
            color = if (days == 0) {MaterialTheme.colorScheme.onPrimaryContainer} else {Color(0xFFFFA500)},
        )
    }
}

@Composable
fun MissionCompleteScreen(
    streak: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Challenge completed!");

        Spacer(modifier = Modifier.height(32.dp));

        Text(text = "Your streak has been increased to $streak" + if (streak == 1) {" day!"} else {" days!"});

        Spacer(modifier = Modifier.height(32.dp));

        Text(text = "Come back tomorrow for the next challenge.");
    }
}

@Composable
fun MissionFailedScreen(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Challenge failed!", textAlign = TextAlign.Center);

        Spacer(modifier = Modifier.height(32.dp));

        Text(text = "Your streak has been reset. You can use a streak saver to preserve your streak.", textAlign = TextAlign.Center);

        Spacer(modifier = Modifier.height(32.dp));

        Text(text = "Come back tomorrow for the next challenge.", textAlign = TextAlign.Center);

        // Text(text = "You can do it!"); AI generated!!!!
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
    ) {

    }

}


@Preview
@Composable
fun MissionCardPreview() {
    MissionsTheme(darkTheme = false) {
        MissionScreen(
            missionRepository = MissionRepository(LocalContext.current),
            userPreferencesRepository = UserPreferencesRepository(LocalContext.current.userPreferencesDataStore),
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

fun getDate(): String {
    val dateFormatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val date = Calendar.getInstance().time
    val dateInString = dateFormatter.format(date)

    return dateInString
}

fun convertToNormalDate(date: String): String {
    val prevDateFormatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val date = prevDateFormatter.parse(date)
    val dateInString = dateFormatter.format(date)

    return dateInString
}

private fun getTimeLeft(): String {

    val millisecondsLeft = getDelay(23, 59, 59)

    return String.format(
        Locale.getDefault(),
        //"%02d:%02d:%02d",
        "%d hours, %d minutes, %d seconds left",
        TimeUnit.MILLISECONDS.toHours(millisecondsLeft),
        TimeUnit.MILLISECONDS.toMinutes(millisecondsLeft - TimeUnit.HOURS.toMillis(TimeUnit.MILLISECONDS.toHours(millisecondsLeft))),
        TimeUnit.MILLISECONDS.toSeconds(millisecondsLeft - TimeUnit.HOURS.toMillis(TimeUnit.MILLISECONDS.toHours(millisecondsLeft)) - TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(millisecondsLeft - TimeUnit.HOURS.toMillis(TimeUnit.MILLISECONDS.toHours(millisecondsLeft)))))
        //TimeUnit.MILLISECONDS.toMinutes(millisecondsLeft) % TimeUnit.HOURS.toMinutes(1),
        //TimeUnit.MILLISECONDS.toSeconds(millisecondsLeft) % TimeUnit.MINUTES.toSeconds(1)
    )
}

//private suspend fun updateTimeLeft(timeLeft: MutableStateFlow<String>) {
    //while ()
//}










