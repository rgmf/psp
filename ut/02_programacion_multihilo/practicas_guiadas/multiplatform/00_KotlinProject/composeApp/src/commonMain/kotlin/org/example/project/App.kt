@file:OptIn(DelicateCoroutinesApi::class, DelicateCoroutinesApi::class)

package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.Key.Companion.Menu
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.MenuBar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.awt.MenuBar
import java.net.HttpURLConnection
import java.net.URL

private fun fetch(url: URL): String {
    Thread.sleep(5000)
    val connection = url.openConnection() as HttpURLConnection

    try {
        connection.requestMethod = "GET"
        connection.connect()

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val stream = connection.inputStream
            return stream.bufferedReader().use { it.readText() }
        } else {
            return "HTTP error code: $responseCode"
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection.disconnect()
    }
    return ""
}

private fun todaysDate(): String {
    fun LocalDateTime.format() = toString().substringBefore('T')

    val now = Clock.System.now()
    val zone = TimeZone.currentSystemDefault()

    return now.toLocalDateTime(zone).format()
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
@Preview
fun App() {
    val netDispatcher = newSingleThreadContext(name = "ServiceCall")
    var data by remember { mutableStateOf("") }

    /*
     * En Jetpack Compose LaunchedEffect está diseñada para trabajar de forma segura con
     * hilos. LaunchedEffect se ejecuta en el CoroutineScope que está vinculado al ciclo de
     * vida del composable y, automáticamente, opera con el hilo principal cuando se actualiza
     * el estado.
     *
     * Se podría usar, también, rememberCoroutineScope y, a través de él, emplear el launch:
     * val scope = rememberCoroutineScope()
     *
     * scope.launch(netDispatcher) { ... }
     */
    LaunchedEffect(netDispatcher) {
        delay(5000)
        data = fetch(URL("https://rickandmortyapi.com/api/character"))
        println("Data: $data")
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Main Thread Bloqueable")
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                data = fetch(URL("https://rickandmortyapi.com/api/character"))
                                println("Data: $data")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Refresh"
                            )
                        }
                    },
                )
            }
        ) { innerPadding ->
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.fillMaxWidth().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Today's date is ${todaysDate()}",
                    modifier = Modifier.padding(20.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Button(onClick = { showContent = !showContent }) {
                    Text("Click me!")
                }
                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                        Text(data)
                    }
                }
            }
        }
    }
}
