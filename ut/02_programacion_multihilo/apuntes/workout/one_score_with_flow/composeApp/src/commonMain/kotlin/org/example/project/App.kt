package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class Player(
    val name: String,
    val score: Int
)

fun generateScores(player: Player): Flow<Int> = flow {
    var currentScore = player.score
    while (true) {
        val increment = Random.nextInt(0, 100)
        currentScore += increment
        emit(currentScore)
        delay(Random.nextLong(2000, 5000))
    }
}

@Composable
fun App() {
    var player by remember { mutableStateOf(Player("Alice", 0)) }

    LaunchedEffect(Unit) {
        val scope = MainScope()
        scope.launch {
            generateScores(player).collect { newScore ->
                player = player.copy(score = newScore)
            }
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Scoring") }) }
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    PlayerInfo(player)
                }
            }
        }
    }
}

@Composable
fun PlayerInfo(player: Player) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(player.name, style = MaterialTheme.typography.h6)
        Text(player.score.toString(), style = MaterialTheme.typography.body1)
    }
}

