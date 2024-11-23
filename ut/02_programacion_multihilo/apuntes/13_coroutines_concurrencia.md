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

# Corrutinas en acción: concurrencia con funciones suspend

## Secuencial por defecto

Por defecto, las **funciones suspend** se ejecutan **en secuencia**, es decir, una detrás de la otra:

```kotlin
suspend fun doNumberOne(): Int {
    delay(1000)
    return 13
}

suspend fun doNumberTwo(): Int {
    delay(1000)
    return 29
}

fun main() = runBlocking {
    val timeMillis = measureTimeMillis {
        val one = doNumberOne()
        val two = doNumberTwo()

        println("$one + $two = ${one + two}")
    }

    println("Terminado en $timeMillis ms")
}
```

Si ejecutas este programa verás que se tardan sobre 2000 ms.

```shell
13 + 29 = 42
Terminado en 2045 ms
```

## Concurrencia son async

El programa anterior genera dos números para sumarlos. La generación de los números son tareas pesadas y da igual el orden en que se generan dichos números (estamos en un escenario asíncrono). Así que podemos convertir el código anterior en concurrente.

Para ello, podemos usar corrutinas y conocemos dos opciones: `launch` y `async`. ¿Por qué elegir `async` para este caso? Porque las funciones `suspend` devuelven resultados.

Entendido lo anterior, ya puedes leer y probar el siguiente código:

```kotlin
suspend fun doNumberOne(): Int {
    delay(1000)
    return 13
}

suspend fun doNumberTwo(): Int {
    delay(1000)
    return 29
}

fun main() = runBlocking {
    val timeMillis = measureTimeMillis {
        val deferredOne = async { doNumberOne() }
        val deferredTwo = async { doNumberTwo() }

        val one = deferredOne.await()
        val two = deferredTwo.await()

        println("$one + $two = ${one + two}")
    }

    println("Terminado en $timeMillis ms")
}
```

> Deberías comprender los cambios que he hecho en el código. Si no es así, no sigas y pregunta dudas.

Al ejecutar esta versión verás que se tardan aproximadamente 1000 ms.

```shell
13 + 29 = 42
Terminado en 1029 ms
```
