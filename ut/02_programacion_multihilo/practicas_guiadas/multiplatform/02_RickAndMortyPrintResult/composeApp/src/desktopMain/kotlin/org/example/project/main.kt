package org.example.project

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.*
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

fun main() = application {
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
        App()
    }
}

object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}