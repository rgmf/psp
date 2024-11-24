# Resultados de aprendizaje y criterios de evaluación

- **RA2**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE2a**. Se han identificado situaciones en las que resulte útil la utilización de varios hilos en un programa.
  - **CE2b**. Se han reconocido los mecanismos para crear, iniciar y finalizar hilos.
  - **CE2c**. Se han programado aplicaciones que implementen varios hilos.
  - **CE2d**. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.
  - **CE2e**. Se han utilizado mecanismos para compartir información entre varios hilos de un mismo proceso.
  - **CE2f**. Se han desarrollado programas formados por varios hilos sincronizados mediante técnicas específicas.
  - **CE2g**. Se ha establecido y controlado la prioridad de cada uno de los hilos de ejecución.
  - **CE2h**. Se han depurado y documentado los programas desarrollados.

# Ejercicio propuesto: descarga de ficheros simulada

Este programa simula la descarga de varios ficheros. Esta descarga se hace secuencialmente y tienes que modificarlo para que la descarga de los ficheros de la lista se haga de forma concurrente por medio de corrutinas.

```kotlin
import kotlin.random.Random

fun downloadFile(fileName: String) {
    val fileSize = Random.nextInt(5, 15) * 100
    var downloaded = 0

    println("Iniciando descarga de $fileName (Tamaño: $fileSize MB)...")

    while (downloaded < fileSize) {
        Thread.sleep(500)
        val chunk = Random.nextInt(10, 50)
        downloaded += chunk
        if (downloaded > fileSize) downloaded = fileSize
        println("$fileName: Descargado ${downloaded}MB/${fileSize}MB")
    }

    println("¡Descarga de $fileName completada!")
}

fun main() {
    val files = listOf("archivo1.zip", "video.mp4", "imagen.png", "documento.pdf")

    println("Iniciando descargas...")

    files.map { fileName ->
        downloadFile(fileName)
    }

    println("Todas las descargas han finalizado.")
}
```
