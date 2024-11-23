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

# Corrutinas en acción: cancelaciones y timeout

En aplicaciones que realizan procesamientos de larga duración a través de corrutinas necesitamos algunos mecanismos de poder cancelar o terminar dichos procesamientos en algunas situaciones. Por ejemplo, imagina que estamos desarrollando un navegador web. Cuando el usuario abre una nueva pestaña y solicita una página web, este procesamiento lo haremos en una corrutina que tendrás que ser interrumpida si el usuario cierra la pestaña antes de que termine de cargarse la página web.

## Cancelación de corrutinas lanzadas con launch

Para cancelar una corrutina lanzada con `launch` usamos el `Job` que nos devuelve:

```kotlin
fun main() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("job: estoy durmiendo $i ...")
            delay(500L)
        }
    }
    
    delay(1300L)
    
    // Cancelamos pasados los 1,3 segundos
    println("main: me cansé de esperar")
    job.cancel()
    job.join()
    
    println("main: se acabó")
}
```

Fíjate que, aunque cancelamos la corrutina, hay que esperar a que finalice, por eso añadimos la instrucción `job.join()` después del `join.cancel()`. Estas dos instrucciones las puedes sustituir por la llamada a `cancelAndJoin()` del objeto `Job`:

```kotlin
// Aquí el código anterior...

// Cancelamos pasados los 1,3 segundos
println("main: me cansé de esperar")
job.cancelAndJoin()
    
println("main: se acabó")
```

## Cancelación de corrutinas lanzadas con async

Hay un cambio importante: si usas `async` recibes como resultado un `Deferred` y no existe el método `cancelAndJoin()`, sino `cancel()` y, luego, `await()` para terminar. No obstante, el método `await()` puede lanzar una excepción si la corrutina terminó antes, por lo que hay que llamar a `await()` en un `try-catch`:

```kotlin
fun main() = runBlocking {
    val deferred = async {
        repeat(1000) { i ->
            println("async: estoy durmiendo $i ...")
            delay(500L)
        }
    }

    delay(1300L)

    // Cancelamos pasados los 1,3 segundos
    println("main: me cansé de esperar")
    deferred.cancel() // Cancelamos la corrutina
    try {
        // Intentamos esperar su resultado (esto lanzará CancellationException si se canceló)
        deferred.await()
    } catch (e: CancellationException) {
        println("main: la tarea fue cancelada")
    }

    println("main: se acabó")
}

```

