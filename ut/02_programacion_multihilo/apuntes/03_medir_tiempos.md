# Medir tiempos de ejecución en Kotlin

Antes de meternos de lleno en la programación multihilo en Kotlin, deja que te hable de una función de Kotlin que te va a resutar útil para medir tiempos de ejecución de fragmentos de código y, de esta manera, poder tomar decisiones sobre la mejor opción.

Dicha función es `measureTimeMillis` y recibe como argumento una función. Esta función que recibe será ejecutada y devolverá el tiempo en milisegundos que se necesito para su ejecución. Por ejemplo:

```kotlin
import kotlin.system.measureTimeMillis

fun main() {
    val timeMillis = measureTimeMillis {
        val numbers = 0..1000

        val n1 = numbers.random()
        val n2 = numbers.randmo()

        val addition = n1 + n2
    }

    println("Tiempo consumido: ${timeMillis / 1000} segundos")
}
```

Fíjate como la función `measureTimeMillis` recibe una lambda que calcula la suma de dos números aleatorios entre 0 y 1000. Lo que obtenemos como resultado de `measureTimeMillis` es el tiempo en milisegundos que tardó en ejecutar esa lambda. Por eso, finalmente dividimos el tiempo entre 1000 para obtener el tiempo en segundos y mostrarlo por pantalla.
