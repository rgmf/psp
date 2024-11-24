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

# Corrutinas en acción: Flow

Un `Flow` es una **secuencia asíncrona de datos** que se produce de forma lenta (elemento por elemento) y **que puede emitir valores, completar o lanzar excepciones**. Se encuentra dentro del paquete `kotlinx.coroutines.flow` y es una evolución de las herramientas de Kotlin para manejar flujos de datos reactivos.

Los `Flow` son fríos (cold), lo que significa que no comienzan a emitir datos hasta que alguien los "colecciona" (consume). Son útiles para manejar secuencias de datos, como flujos de eventos, actualizaciones de estado o resultados de una operación que no están disponibles de inmediato.

> Guardando las distancias, es algo parecido al Patrón Observer, donde hay un publicador publicando valores/eventos y una serie de consumidores suscritos a dichos eventos.

## Ejemplo práctico: simulación de un sensor de temperatura

Imágenes que tenemos un sensor de temperaturas que va emitiendo las temperaturas y, cuando eso suceda, las queremos capturar para mostrarlas por pantalla.

En este caso, la generación de los valores de las temperaturas lleva su tiempo y para no bloquear el hilo principal y que podamos estar haciendo otras tareas mientras se van generando las temperaturas vamos a usar `Flow`.

```kotlin
fun temperatureSensor(): Flow<Int> = flow {
    while (true) {
        val temperature = Random.nextInt(-10, 35)
        emit(temperature)
        delay(1000)
    }
}

fun main() = runBlocking {
    println("Iniciando lectura de temperaturas...")

    temperatureSensor()
        .filter { it >= 0 }                   // Filtramos solo temperaturas mayores o iguales a 0
        .map { "Temperatura actual: $it°C" }  // Transformamos los datos en un mensaje
        .take(5)                              // Limitamos a 5 emisiones
        .collect { println(it) }              // Mostramos las temperaturas
}

```

En este ejemplo tenemos una función llamada `temperatureSensor` que es un generador de temperaturas infinito. No deja de generar y emitir temperaturas (fíjate en el bucle `while (true)`).

Este generador lo usamos en la función principal. Fíjate, además, que no solo consumimos desde el `main` las temperaturas que genera sino que se están usando una serie de operadores de la clase `Flow` (cada operador está comentado).

Al final, este programa, consume 5 temperaturas:

```shell
Iniciando lectura de temperaturas...
Temperatura actual: 24°C
Temperatura actual: 23°C
Temperatura actual: 24°C
Temperatura actual: 18°C
Temperatura actual: 21°C
```

Mientras se van emitiendo y consumiendo estas temperaturas, nuestro programa podría estar haciendo otras tareas en otra corrutina. Aquí tienes el ejemplo anterior en la que se ha añadido una nueva corrutina que imprime por pantalla un mensaje cada medio segundo:

```kotlin
fun temperatureSensor(): Flow<Int> = flow {
    while (true) {
        val temperature = Random.nextInt(-10, 35)
        emit(temperature)
        delay(1000)
    }
}

fun main() = runBlocking {
    println("Sistema iniciado. Monitoreando temperatura...")

    launch {
        repeat(10) {
            println("Una corrutina corriendo concurrentemente con el generador...")
            delay(500)
        }
    }

    temperatureSensor()
        .filter { it >= 0 }
        .map { "Temperatura actual: $it°C" }
        .take(5)
        .collect { println(it) }

    println("Tarea completada.")
}

```

Al ejecutar este código obtendrás algo así por pantalla, lo que demuestra que se han ejecutado las corrutinas concurrentemente o de forma asíncrona:

```kotlin
Sistema iniciado. Monitoreando temperatura...
Temperatura actual: 2°C
Una corrutina corriendo concurrentemente con el generador...
Una corrutina corriendo concurrentemente con el generador...
Temperatura actual: 14°C
Una corrutina corriendo concurrentemente con el generador...
Una corrutina corriendo concurrentemente con el generador...
Temperatura actual: 16°C
Una corrutina corriendo concurrentemente con el generador...
Una corrutina corriendo concurrentemente con el generador...
Temperatura actual: 11°C
Una corrutina corriendo concurrentemente con el generador...
Una corrutina corriendo concurrentemente con el generador...
Temperatura actual: 4°C
Tarea completada.
Una corrutina corriendo concurrentemente con el generador...
Una corrutina corriendo concurrentemente con el generador...
 ```
