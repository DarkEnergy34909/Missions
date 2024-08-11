package com.example.missions

import android.widget.CheckBox
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.missions.data.DataSource
import com.example.missions.ui.theme.MissionsTheme
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

@Composable
fun MissionScreen(modifier: Modifier = Modifier) {
    var missionState by remember {mutableStateOf(MissionStates.MISSION_UNDEFINED.ordinal)}

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = Calendar.getInstance().time
    val dateInString = dateFormatter.format(date)

    Scaffold(
        topBar = {
            MissionAppBar(
                canNavigateBack = false,
            )
        },
        modifier = modifier
            .statusBarsPadding()
            .padding(dimensionResource(R.dimen.padding_small))
    ) {innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            //MenuBar()

            //Spacer(modifier = Modifier.size(256.dp))
            if (missionState == MissionStates.MISSION_UNDEFINED.ordinal) {
                Streak(0);

                MissionCard(DataSource.missions[Random.nextInt(0, DataSource.missions.size)], dateInString)

                SuccessFailureButtons(
                    onFailButtonPressed = {missionState = MissionStates.MISSION_FAILED.ordinal},
                    onSuccessButtonPressed = {missionState = MissionStates.MISSION_COMPLETED.ordinal}
                )
            }
            else if (missionState == MissionStates.MISSION_COMPLETED.ordinal) {

            }


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
            Text(text = "Missions")
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
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    );
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
    missionText: String,
    date: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
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
                        .padding(start = dimensionResource(R.dimen.padding_small), end = dimensionResource(R.dimen.padding_small))
                )


                /*Text(
                    text = "PEE",
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = dimensionResource(R.dimen.padding_small), end = dimensionResource(R.dimen.padding_small))
                )*/

            }
            Text(
                text = missionText,
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
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = "Streak"
        )
        
        Text(
            text = days.toString()
        )
    }
}

@Composable
fun MissionCompleteScreen(modifier: Modifier = Modifier) {

}


@Preview
@Composable
fun MissionCardPreview() {
    MissionsTheme {
        MissionScreen()
    }
}


