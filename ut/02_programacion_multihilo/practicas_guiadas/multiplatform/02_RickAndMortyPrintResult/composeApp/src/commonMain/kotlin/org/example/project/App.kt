package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import java.net.HttpURLConnection
import java.net.URL

class FetchRunnable(val url: URL): Runnable {
    private var _data: String = ""
    val data: String
        get() = _data

    override fun run() {
        println("Start running in thread ${Thread.currentThread().name}")
        Thread.sleep(5000)
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val stream = connection.inputStream
                _data = stream.bufferedReader().use { it.readText() }
            } else {
                _data = "HTTP error code: $responseCode"
            }
            println("Data from thread ${Thread.currentThread().name}: $_data")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
    }
}

@Composable
@Preview
fun App() {
    var data by remember { mutableStateOf("") }
    val task = FetchRunnable(URL("https://rickandmortyapi.com/api/character"))

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
                                val thread = Thread(task)
                                thread.start()
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Información obtenida en Rick and Morty API:\n ${data}",
                    modifier = Modifier.padding(20.dp),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Button(onClick = { showContent = !showContent }) {
                    Text("¡Haz Clic Aquí!")
                }
                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Text("Compose: $greeting")
                    }
                }
            }
        }
    }
}
