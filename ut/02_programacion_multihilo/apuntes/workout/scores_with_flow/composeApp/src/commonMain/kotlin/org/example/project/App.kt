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
    val players = remember {
        mutableStateListOf(
            Player("Alice", 0),
            Player("Bob", 0),
            Player("Mary", 0)
        )
    }

    LaunchedEffect(players) {
        val scope = MainScope()
        players.forEachIndexed { index, player ->
            scope.launch {
                generateScores(player).collect { newScore ->
                    players[index] = player.copy(score = newScore)
                }
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
                    players.forEach { player ->
                        PlayerInfo(player)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Puntuación Total: ${players.map { it.score }.sum()}",
                    style = MaterialTheme.typography.h6
                )
                Text(
                    "Media de puntos: ${players.map { it.score }.sum() / players.size}",
                    style = MaterialTheme.typography.h6
                )
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

