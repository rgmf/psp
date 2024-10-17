# Resultados de aprendizaje y criterios de evaluación

- **RA2**. Desarrolla aplicaciones compuestas por varios procesos reconociendo y aplicando principios de programación paralela.
  - **CE2b**. Se han reconocido los mecanismos para crear, iniciar y finalizar hilos.
  - **CE2c**. Se han programado aplicaciones que implementen varios hilos.
  - **CE2d**. Se han identificado los posibles estados de ejecución de un hilo y programado aplicaciones que los gestionen.

# La clase Thread

Para manejar hilos (*threads*) directamente en Kotlin usamos la biblioteca estándar de Kotlin que tiene compatibilidad con la de Java. Y se hace a través de estas clases: `Thread`, `Runnable` y `ExecutorService` (y `Executors`).

Aquí nos vamos a centrar en la clase `Thread`.

# Ejemplo básico

Te muestro directamente un ejemplo de hilo básico utilizando la clase `Thread` de Kotlin donde verás que:

- La clase `Thread` recibe una función lambda con el código que ejecutará el hilo creado.
- En este caso el hilo no realiza ninguna tarea útil, simplemente se simula que está haciendo una tarea que dura 1 segundo (1 000 milisegundos).
- Los procesos son identificados por un nombre al que podemos acceder a través de la clase `Thread` (`Thread.currentThread().name`)
- Para iniciar la ejecución del hilo se usa el método `start()`.
- El hilo ejecutado con el método `start()` se ejecuta en segundo plano.

> El término **segundo plano** en este contexto de hilos significa que se ejecuta sin interferir ni bloquear el hilo principal. Se dice también que se ejecuta de forma **asíncrona**.

```kotlin
fun main() {
    val thread = Thread {
        println("Tarea ejecutándose en un hilo: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        println("Tarea completada en un hilo: ${Thread.currentThread().name}")
    }
    thread.start()
    println("Hilo principal: ${Thread.currentThread().name}")
}
```

Podemos hacer que el hilo principal espere a que termine el hilo hijo con el método `join()`:

```kotlin
fun main() {
    val thread = Thread {
        println("Tarea ejecutándose en un hilo: ${Thread.currentThread().name}")
        Thread.sleep(1000)
        println("Tarea completada en un hilo: ${Thread.currentThread().name}")
    }
    thread.start()
    thread.join()
    println("Hilo principal: ${Thread.currentThread().name}")
}
```

Ahora, el hilo principal espera a que termine el hilo hijo y, por tanto, verás que es el último en finalizar.

# Memoria compartida

Para explicar y entender cómo se usa la memoria compartida entre hilos para la comunicación entre ellos, pasemos a un ejemplo donde un programa busca los números primos entre el 1 y el 200. En este programa se divide la tarea en dos hilos: uno busca los números primos que hay entre el 1 y el 100 y el otro entre el 101 y el 200. Varias cosas hay que señalar aquí:

- Por un lado verás que se usan variables creadas en el hilo principal en los hilos hijos porque, como ya sabes, la memoria es compartida por los hilos, así que todos los hilos pueden acceder a las variables creadas por el hilo padre.

- Además, ves que estoy usando dos variables diferentes que serán usadas por cada hilo hijo. No debemos usar la misma memoria por dos hilos que se ejecutan al mismo tiempo de forma concurrente. Veremos más adelante cómo mejorar el uso compartido de memoria.

```kotlin
/**
 * Función que comprueba si un número es primo.
 */
fun isPrime(num: Int): Boolean {
    if (num < 2) {
        return false
    }

    for (i in 2..Math.sqrt(num.toDouble()).toInt()) {
        if (num % i == 0) {
            return false
        }
    }

    return true
}

/**
 * Función de entrada al programa.
 */
fun main() {
    // Listas para almacenar los números primos de cada rango
    val primes1 = mutableListOf<Int>()
    val primes2 = mutableListOf<Int>()

    // Hilo para buscar primos en el rango 1-100
    val thread1 = Thread {
        for (i in 1..100) {
            if (isPrime(i)) {
                primes1.add(i)
            }
        }
        println("Hilo ${Thread.currentThread().name} completado. Primos de 1 a 100 encontrados.")
    }

    // Hilo para buscar primos en el rango 101-200
    val thread2 = Thread {
        for (i in 101..200) {
            if (isPrime(i)) {
                primes2.add(i)
            }
        }
        println("Hilo ${Thread.currentThread().name} completado. Primos de 101 a 200 encontrados.")
    }

    // Iniciar los hilos
    thread1.start()
    thread2.start()

    // Esperar a que ambos hilos terminen
    thread1.join()
    thread2.join()

    // Combinar y mostrar los resultados
    val primes = primes1 + primes2
    println("Números primos entre 1 y 200: $primes")
}
```
