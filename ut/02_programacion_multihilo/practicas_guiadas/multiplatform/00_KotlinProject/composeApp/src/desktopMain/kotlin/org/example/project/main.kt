package org.example.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.*
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    var action by remember { mutableStateOf("Last action: None") }
    val trayState = rememberTrayState()

    Tray(
        state = trayState,
        icon = TrayIcon
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Damfound",
        icon = painterResource(Res.drawable.compose_multiplatform)
    ) {
        MenuBar {
            Menu("File", mnemonic = 'F') {
                Item(
                    "Copy",
                    onClick = { action = "Last action: Copy" },
                    shortcut = KeyShortcut(Key.C, ctrl = true)
                )
                Item(
                    "Paste",
                    onClick = { action = "Last action: Paste" },
                    shortcut = KeyShortcut(Key.V, ctrl = true)
                )
            }
        }
        App()
    }
}

object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}