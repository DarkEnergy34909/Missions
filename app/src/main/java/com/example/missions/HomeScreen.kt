package com.example.missions

import android.widget.CheckBox
import androidx.annotation.StringRes
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.missions.ui.theme.MissionsTheme


@Composable
fun MissionScreen(modifier: Modifier = Modifier) {

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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            //MenuBar()

            //Spacer(modifier = Modifier.size(256.dp))

            MissionCard("Talk to a stranger today.")

            SuccessFailureButtons()


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
            Text(text = "Talkative.")
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
fun SuccessFailureButtons(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Spacer(modifier = Modifier.weight(0.5f))

        ActionButton(
            imageVector = Icons.Filled.Close,
            onClick = {},
            color = Color.Red,
            modifier = Modifier
                .weight(1f)
        )

        Spacer(modifier = Modifier.weight(2f))

        ActionButton(
            imageVector = Icons.Filled.Check,
            onClick = {},
            color = Color.Green,
            modifier = Modifier
                .weight(1f)
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
    missionNumber: Int = 0,
    dayNumber: Int = 0,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
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
                    text = "08/07/2024",
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


@Preview
@Composable
fun MissionCardPreview() {
    MissionsTheme {
        MissionScreen()
    }
}


