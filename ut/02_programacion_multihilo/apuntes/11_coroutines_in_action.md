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

# Corrutinas en acción: iniciación

El objetivos final de las corrutinas es el de optimizar al máximo el uso de hilos a través de la programación que no bloquee ninguno de esos hilos.

Cuando escribimos programas multihilo, como ya sabes, se pueden bloquear dichos hilos porque estén haciendo operaciones bloqueantes. En el caso de las corrutinas, si hay partes bloqueantes, el hilo que lo está atendiendo se puede ir a atender otras cosas, con lo que se optimiza el tiempo de ejecución de los hilos.

Esto es lo que en Kotlin se llama *Suspendable Computations*, que son operaciones que pueden ser suspendidas sin bloquear ningún hilo en ejecución. Como he comentado en el párrafo anterior, esto supone que si una operación que está ejecutando el hilo X es suspendida, dicho hilo X en vez de esperar, es liberado para realizar otras operaciones. Cuando la operación en suspensión continua su ejecución, cualquier otro hilo continuará su ejecución.

## Tu primera corrutina

Una **corrutina es una instancia de un cálculo suspendible** (*suspendable computation*). Conceptualmente es similar a un hilo, en el sentido que toma el bloque de código a ejecutar y lo ejecuta concurrentemente. Sin emabargo, una corrutina no está enlazada a un hilo en concreto. Como ya he comentado varias veces, una corrutina puede empezar a ser ejecutado en un hilo X y terminar por ser ejecutado por un hilo Y.

Así, podemos ver a las corrutinas como hilos ligeros.

Ahora sí, esta sería tu primera corrutina en Kotlin:

```kotlin
fun main() = runBlocking {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello")
}
```

Varias cosas sobre este código para ir entendiendo cómo se usan las corrutinas en Kotlin:

- `launch` es una construcción del lenguaje de Kotlin (una primitiva) que permite lanzar y dar comienzo a la corrutina de forma asíncrona (`launch` es lo que en Kotlin denoninamos *coroutine builder*)
- `delay` es una función suspendible (*suspending function*) y, por tanto, solo se puede usar dentro de corrutinas (en este caso la usamos para suspender la rutina 1 segundo)
- `runBlocking` es, también, una *coroutine builder* que hace de puente entre el "mundo de las corrutinas" y una función regular como es `main`
- La instrucción `println("Hello")` es lanzada en la corrutina principal

Por tanto, en este código, tenemos dos bloques de código lanzadas asíncronamente: lo que hay dentro del `launch` por un lado y el resto del código por el otro.

Antes de seguir es importante que entiendas qué hace, con más detalle, la *coroutine builder* `runBlocking`. Es, a menudo, usado en funciones de primer nivel, como en nuestro ejemplo anterior, porque lo que hace es suspender el hilo que ejecuta dicha función hasta que termine la corrutina.

Más adelante te doy más detalles de `runBlocking`.

## Tu primera función suspendible

Cuando trabajamos con corrutinas, tenemos que marcar como suspendibles aquellas funciones que usen código suspendible. Por ejemplo, si refactorizamos el código de nuestro primer ejemplo para sacarla a una función el trabajo realizado dentro del `launch`, tendríamos que usar el modificador o primitiva `suspend` como ves aquí:

```kotlin
suspend fun doWorld() {
    delay(1000L)
    println("World!")
}

fun main() = runBlocking {
    launch {
        doWorld()
    }
    println("Hello")
}
```

Así, `doWorld` es una función suspendible porque realiza tareas que pueden ser suspendidas. Estas funciones `suspend` pueden ser ejecutadas dentro de una *coroutine builder*.

## Construcción de contexto o scope builder: coroutineScope y runBlocking

Puedes construir tu propio contexto de corrutinas con la primitiva de Kotlin `coroutineScope`.

La pregunta ahora es: ¿qué diferencia hay entre `coroutineScope` y `runBlocking`? Básicamente `runBlocking` bloquea al hilo principal hasta que se ejecute la función donde se usa, mientras que `coroutineScope` suspende el hilo que lo ejecuta por lo que no lo bloquea y dicho hilo puede pasar a ejecutar otras operaciones o tareas.

El uso típico de `runBlocking` se da en los tests y la función principal del programa para iniciar las corrutinas, mientras que `coroutineScope` solo se puede usar en funciones suspendibles o `suspend`. En la tabla siguiente te resumo las características y uso de cada una:

| Puntos a tratar           | `runBlocking`                                     | `coroutineScope`                                  |
| ------------------------- | ------------------------------------------------- | ------------------------------------------------- |
| ¿Bloquea el hilo?         | Sí                                                | No                                                |
| Uso típico                | Entrada al programa o pruebas                       | Dentro de funciones `suspend`                      |
| Contexto necesario        | Puede ser llamado desde cualquier lugar           | Solo se puede usar en funciones `suspend`         |
| Concurrencia estructurada | Garantiza completar todas las corrutinas internas | Garantiza completar todas las corrutinas internas |

Por último, un ejemplo:

```kotlin
/**
 * Función suspend que lanza 2 corrutinas asíncronamente
 */
suspend fun doWorld() = coroutineScope {
    launch {
        delay(2000L)
        println("World 2")
    }
    launch {
        delay(1000L)
        println("World 1")
    }
    println("Hello")
}

/**
 * Función de entrada al programa con contexto para corrutinas
 */
fun main() = runBlocking {
    doWorld()
    println("Done")
}
```

## Esperando a las corrutinas: la clase Job

El *coroutine builder* `launch` devuelve un objeto de la clase `Job` que puede ser usado de explícitamente para esperar a que la corrutina termine.

```kotlin
fun main() = runBlocking {
    // Capturamos el Job que devuelve la corrutina al terminar
    val job = launch {
        delay(1000L)
        println("World!")
    }

    println("Hello")
    
    // Esperamos a que termine la corrutina anterior
    job.join()
    
    println("Done") 
```

## Async: un launch que devuelve valores

Hasta ahora solo hemos visto la función `launch` para lanzar corrutinas pero tenemos, también, otra función llamada `async`, que funciona igual pero que ofrece la posibilidad de recibir valores como resultados (como hace un `return` en una función común).

Así pues, si quieres crear una corrutina que haga una tarea que no devuelve resultados usa `launch`. Si tienes que crear una corrutina que devuelva un resultado usa `async`.

> Como ya sabes `launch` devuelve un objeto del tipo `Job` pero dicho objeto solo se usa para esperar a la corrutina o cancelarla, por ejemplo.

Las corrutinas `async` devuelven un objeto del tipo `Deferred`, que es una especie de "futuro" o "promesa" donde estará el valor resultante de la corrutina. Para obtener ese valor tendrás que emplear el método `await` como ves en este ejemplo:

```kotlin
fun main() = runBlocking {
    val deferred = async {
        delay(1000L)
        "Result of async task"
    }
    
    println("Task started")
    
    // Espera el resultado y lo imprime por pantalla
    val result = deferred.await()
    println(result)
}
```

Y, por último, para que puedas comparar en el mismo programan un "launch" y un "async" te dejo este ejemplo:

```kotlin
import kotlinx.coroutines.*
import kotlin.random.Random

fun main() = runBlocking {
    val job = launch {
        val randomNumber = Random.nextInt(1, 100)
        println("Launch generated: $randomNumber")
    }
    
    val deferred = async {
        Random.nextInt(1, 100) // Este será el valor devuelto
    }

    job.join()
    
    // Obtenemos el resultado de la tarea async
    val asyncResult = deferred.await()
    println("Async generated: $asyncResult")
}
```
