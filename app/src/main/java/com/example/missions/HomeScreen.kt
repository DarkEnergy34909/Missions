package com.example.missions

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.missions.data.DataSource
import com.example.missions.data.Mission
import com.example.missions.data.MissionRepository
import com.example.missions.ui.theme.MissionsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

//@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MissionScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val repository = remember{ MissionRepository(context) }

    var missionState by remember {mutableStateOf(MissionStates.MISSION_UNDEFINED.ordinal)}

    //var currentMission: Mission by remember {mutableStateOf(DataSource.missions[Random.nextInt(0, DataSource.missions.size)])}
    var currentMission: Mission by remember {mutableStateOf(Mission(text = "Loading...", difficulty = "Easy", completed = false, dateCompleted = ""))}

    LaunchedEffect(scope) {
        //DataSource.addMissionsToDatabase(repository)
        currentMission = getNewMission(repository)
    }


    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = MissionScreens.valueOf(
        backStackEntry?.destination?.route ?: MissionScreens.Home.name
    )

    Scaffold(
        topBar = {
            MissionAppBar(
                canNavigateBack = false,
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
                    repository = repository,
                    currentMission = currentMission,
                    missionState = missionState,
                    completeMission = {
                        missionState = MissionStates.MISSION_COMPLETED.ordinal
                        currentMission.completed = true
                        scope.launch{
                            repository.updateMission(currentMission)
                        }
                                      },

                    failMission = {
                        missionState = MissionStates.MISSION_FAILED.ordinal
                        currentMission.failed = true
                        scope.launch{
                            repository.updateMission(currentMission)
                        }
                                  },

                    modifier = modifier
                )
            }
            composable(route = MissionScreens.Add.name) {
                AddScreen(
                    repository = repository,
                    modifier = modifier
                )
            }
            composable(route = MissionScreens.History.name) {
                HistoryScreen(
                    repository = repository,
                    //previousMissions = DataSource.missions,
                    modifier = modifier
                )
            }
            composable(route = MissionScreens.More.name) {
                //MoreScreen() TODO: More screen
            }
        }
    }

}

@Composable
fun HomeScreen(
    repository: MissionRepository,
    missionState: Int,
    completeMission: () -> Unit,
    failMission: () -> Unit,
    currentMission: Mission,
    //innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = Calendar.getInstance().time
    val dateInString = dateFormatter.format(date)

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

            MissionCard(
                mission = currentMission  /*DataSource.missions[Random.nextInt(0, DataSource.missions.size)]*/,
                date = dateInString,
                previous = false
            )

            SuccessFailureButtons(
                onFailButtonPressed = failMission     /*{missionState = MissionStates.MISSION_FAILED.ordinal}*/,
                onSuccessButtonPressed = completeMission  /*{missionState = MissionStates.MISSION_COMPLETED.ordinal}*/
            )
        }
        else if (missionState == MissionStates.MISSION_COMPLETED.ordinal) {
            MissionCompleteScreen()
        }
        else {
            MissionFailedScreen()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionAppBar(
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
                        imageVector = Icons.Filled.Menu,
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
            Streak(32)
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
            }
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
fun MissionCompleteScreen(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Challenge completed!");

        Spacer(modifier = Modifier.height(32.dp));

        Text(text = "Your streak has been increased to x days.");

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
        MissionScreen()
    }
}

private suspend fun getNewMission(repository: MissionRepository): Mission {
    val missionList = repository.getUncompletedMissions()
    return missionList[Random.nextInt(0, missionList.size)]
}


